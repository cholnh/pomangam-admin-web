<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@page import="java.util.List"%>
<%@page import="com.google.gson.Gson"%>
<div class="px-content">
	<div class="page-header">
		<h1>
			<span class="text-muted font-weight-light"><i
				class="page-header-icon ion-person-stalker"></i>운영자 관리 / </span>권한 설정
		</h1>
	</div>
	<div class="row">
		<div class="panel">
			<div class="panel-heading">
				<div class="row">
					<div class="col-md-2">
						<div class="box-cell valign-middle font-size-20">운영자 선택</div>
					</div>
					<div class="col-md-4">
						<select class="form-control input-lg" id="authnameSelector">
							<option>Not selected</option>
							<%
							@SuppressWarnings({"unchecked"})
							List<String> authlist = (List<String>) request.getAttribute("authnames");
							for(String authname : authlist) {%>
								<option><%=authname %></option>
							<%}%>
						</select>
					</div>
					<div class="col-md-4">
						<a class="btn btn-success" data-toggle="modal" id="create"
							data-backdrop="static" data-target="#authorityInfo">
							<i class="ion-ios-plus-outline"></i> 
							<span>등록</span>
						</a>
						<a class="btn btn-danger" id="delete" onclick="deleteAuth()">
							<i class="ion-ios-close-outline"></i>
							<span>삭제</span>
						</a>
						<button class="btn btn-default" type="button" name="refresh" 
							id="refresh" title="새로고침" style="height:31px">
							<i class="glyphicon ion-refresh"></i>
						</button>
					</div>
				</div>
			</div>
			<div class="panel-body" id="authorityBody" style="display:none">
				<form>
				<div class="table-responsive">
					<table class="table table-bordered">
						<thead>
							<tr>
								<th class="font-size-13">대분류</th>
								<th class="font-size-13">접근</th>
								<th class="font-size-13">소분류</th>
								<th class="font-size-13">보기기능</th>
								<th class="font-size-13">편집기능</th>
							</tr>
						</thead>
						<tbody>
							<tr>
								<th class="font-size-13 valign-middle" rowspan="3">매장 관리</th>
								<td class="valign-middle" rowspan="3"><label
									class="switcher switcher-rounded switcher-lg switcher-primary switcher-blank">
										<input type="checkbox" id="menu_restaurant" checked>
										<div class="switcher-indicator">
											<div class="switcher-yes">ON</div>
											<div class="switcher-no">OFF</div>
										</div>
								</label></td>
								<th class="font-size-13">주문 현황</th>
								<td><label
									class="switcher switcher-rounded switcher-lg switcher-primary switcher-blank">
										<input type="checkbox" id="menu0-1" checked>
										<div class="switcher-indicator">
											<div class="switcher-yes">ON</div>
											<div class="switcher-no">OFF</div>
										</div>
								</label></td>
								<td><label
									class="switcher switcher-rounded switcher-lg switcher-primary switcher-blank">
										<input type="checkbox" id="menu0-2" checked>
										<div class="switcher-indicator">
											<div class="switcher-yes">ON</div>
											<div class="switcher-no">OFF</div>
										</div>
								</label></td>
							</tr>
							<tr>
								<th class="font-size-13">메뉴 관리</th>
								<td><label
									class="switcher switcher-rounded switcher-lg switcher-primary switcher-blank">
										<input type="checkbox" id="menu1-1" checked>
										<div class="switcher-indicator">
											<div class="switcher-yes">ON</div>
											<div class="switcher-no">OFF</div>
										</div>
								</label></td>
								<td><label
									class="switcher switcher-rounded switcher-lg switcher-primary switcher-blank">
										<input type="checkbox" id="menu1-2" checked>
										<div class="switcher-indicator">
											<div class="switcher-yes">ON</div>
											<div class="switcher-no">OFF</div>
										</div>
								</label></td>
							</tr>
							<tr>
								<th class="font-size-13">메출 관리</th>
								<td><label
									class="switcher switcher-rounded switcher-lg switcher-primary switcher-blank">
										<input type="checkbox" id="menu2-1" checked>
										<div class="switcher-indicator">
											<div class="switcher-yes">ON</div>
											<div class="switcher-no">OFF</div>
										</div>
								</label></td>
								<td><label
									class="switcher switcher-rounded switcher-lg switcher-primary switcher-blank">
										<input type="checkbox" id="menu2-2" checked>
										<div class="switcher-indicator">
											<div class="switcher-yes">ON</div>
											<div class="switcher-no">OFF</div>
										</div>
								</label></td>
							</tr>

							<tr>
								<th class="font-size-13 valign-middle" rowspan="2">배달 관리</th>
								<td rowspan="2" class="valign-middle"><label
									class="switcher switcher-rounded switcher-lg switcher-primary switcher-blank">
										<input type="checkbox" id="menu_porter" checked>
										<div class="switcher-indicator">
											<div class="switcher-yes">ON</div>
											<div class="switcher-no">OFF</div>
										</div>
								</label></td>
								<th class="font-size-13">주문 현황</th>
								<td><label
									class="switcher switcher-rounded switcher-lg switcher-primary switcher-blank">
										<input type="checkbox" id="menu3-1" checked>
										<div class="switcher-indicator">
											<div class="switcher-yes">ON</div>
											<div class="switcher-no">OFF</div>
										</div>
								</label></td>
								<td><label
									class="switcher switcher-rounded switcher-lg switcher-primary switcher-blank">
										<input type="checkbox" id="menu3-2" checked>
										<div class="switcher-indicator">
											<div class="switcher-yes">ON</div>
											<div class="switcher-no">OFF</div>
										</div>
								</label></td>
							</tr>
							
							<tr>
								<th class="font-size-13">매출 정산</th>
								<td><label
									class="switcher switcher-rounded switcher-lg switcher-primary switcher-blank">
										<input type="checkbox" id="menu4-1" checked>
										<div class="switcher-indicator">
											<div class="switcher-yes">ON</div>
											<div class="switcher-no">OFF</div>
										</div>
								</label></td>
								<td><label
									class="switcher switcher-rounded switcher-lg switcher-primary switcher-blank">
										<input type="checkbox" id="menu4-2" checked>
										<div class="switcher-indicator">
											<div class="switcher-yes">ON</div>
											<div class="switcher-no">OFF</div>
										</div>
								</label></td>
							</tr>
							
							<tr>
								<th rowspan="4" class="font-size-13 valign-middle">회원 관리</th>
								<td rowspan="4" class="valign-middle"><label
									class="switcher switcher-rounded switcher-lg switcher-primary switcher-blank">
										<input type="checkbox" id="menu_member" checked>
										<div class="switcher-indicator">
											<div class="switcher-yes">ON</div>
											<div class="switcher-no">OFF</div>
										</div>
								</label></td>
								<th class="font-size-13">회원 관리</th>
								<td><label
									class="switcher switcher-rounded switcher-lg switcher-primary switcher-blank">
										<input type="checkbox" id="menu5-1" checked>
										<div class="switcher-indicator">
											<div class="switcher-yes">ON</div>
											<div class="switcher-no">OFF</div>
										</div>
								</label></td>
								<td><label
									class="switcher switcher-rounded switcher-lg switcher-primary switcher-blank">
										<input type="checkbox" id="menu5-2" checked>
										<div class="switcher-indicator">
											<div class="switcher-yes">ON</div>
											<div class="switcher-no">OFF</div>
										</div>
								</label></td>
							</tr>
							<tr>
								<th class="font-size-13">점주 관리</th>
								<td><label
									class="switcher switcher-rounded switcher-lg switcher-primary switcher-blank">
										<input type="checkbox" id="menu6-1" checked>
										<div class="switcher-indicator">
											<div class="switcher-yes">ON</div>
											<div class="switcher-no">OFF</div>
										</div>
								</label></td>
								<td><label
									class="switcher switcher-rounded switcher-lg switcher-primary switcher-blank">
										<input type="checkbox" id="menu6-2" checked>
										<div class="switcher-indicator">
											<div class="switcher-yes">ON</div>
											<div class="switcher-no">OFF</div>
										</div>
								</label></td>
							</tr>
							<tr>
								<th class="font-size-13">운영자 관리</th>
								<td><label
									class="switcher switcher-rounded switcher-lg switcher-primary switcher-blank">
										<input type="checkbox" id="menu7-1" checked>
										<div class="switcher-indicator">
											<div class="switcher-yes">ON</div>
											<div class="switcher-no">OFF</div>
										</div>
								</label></td>
								<td><label
									class="switcher switcher-rounded switcher-lg switcher-primary switcher-blank">
										<input type="checkbox" id="menu7-2" checked>
										<div class="switcher-indicator">
											<div class="switcher-yes">ON</div>
											<div class="switcher-no">OFF</div>
										</div>
								</label></td>
							</tr>
							<tr>
								<th class="font-size-13">권한 관리</th>
								<td><label
									class="switcher switcher-rounded switcher-lg switcher-primary switcher-blank">
										<input type="checkbox" id="menu8-1" checked>
										<div class="switcher-indicator">
											<div class="switcher-yes">ON</div>
											<div class="switcher-no">OFF</div>
										</div>
								</label></td>
								<td><label
									class="switcher switcher-rounded switcher-lg switcher-primary switcher-blank">
										<input type="checkbox" id="menu8-2" checked>
										<div class="switcher-indicator">
											<div class="switcher-yes">ON</div>
											<div class="switcher-no">OFF</div>
										</div>
								</label></td>
							</tr>
							<tr>
								<th rowspan="2" class="font-size-13 valign-middle">지역 관리</th>
								<td rowspan="2" class="valign-middle"><label
									class="switcher switcher-rounded switcher-lg switcher-primary switcher-blank">
										<input type="checkbox" id="menu_target" checked>
										<div class="switcher-indicator">
											<div class="switcher-yes">ON</div>
											<div class="switcher-no">OFF</div>
										</div>
								</label></td>
								<th class="font-size-13">지역 설정</th>
								<td><label
									class="switcher switcher-rounded switcher-lg switcher-primary switcher-blank">
										<input type="checkbox" id="menu9-1" checked>
										<div class="switcher-indicator">
											<div class="switcher-yes">ON</div>
											<div class="switcher-no">OFF</div>
										</div>
								</label></td>
								<td><label
									class="switcher switcher-rounded switcher-lg switcher-primary switcher-blank">
										<input type="checkbox" id="menu9-2" checked>
										<div class="switcher-indicator">
											<div class="switcher-yes">ON</div>
											<div class="switcher-no">OFF</div>
										</div>
								</label></td>
							</tr>
							<tr>
								<th class="font-size-13">매장 설정</th>
								<td><label
									class="switcher switcher-rounded switcher-lg switcher-primary switcher-blank">
										<input type="checkbox" id="menu10-1" checked>
										<div class="switcher-indicator">
											<div class="switcher-yes">ON</div>
											<div class="switcher-no">OFF</div>
										</div>
								</label></td>
								<td><label
									class="switcher switcher-rounded switcher-lg switcher-primary switcher-blank">
										<input type="checkbox" id="menu10-2" checked>
										<div class="switcher-indicator">
											<div class="switcher-yes">ON</div>
											<div class="switcher-no">OFF</div>
										</div>
								</label></td>
							</tr>
							
							<tr>
								<th rowspan="1" class="font-size-13 valign-middle">쿠폰 관리</th>
								<td rowspan="1" class="valign-middle"><label
									class="switcher switcher-rounded switcher-lg switcher-primary switcher-blank">
										<input type="checkbox" id="menu_coupon" checked>
										<div class="switcher-indicator">
											<div class="switcher-yes">ON</div>
											<div class="switcher-no">OFF</div>
										</div>
								</label></td>
								<th class="font-size-13">쿠폰 발급</th>
								<td><label
									class="switcher switcher-rounded switcher-lg switcher-primary switcher-blank">
										<input type="checkbox" id="menu11-1" checked>
										<div class="switcher-indicator">
											<div class="switcher-yes">ON</div>
											<div class="switcher-no">OFF</div>
										</div>
								</label></td>
								<td><label
									class="switcher switcher-rounded switcher-lg switcher-primary switcher-blank">
										<input type="checkbox" id="menu11-2" checked>
										<div class="switcher-indicator">
											<div class="switcher-yes">ON</div>
											<div class="switcher-no">OFF</div>
										</div>
								</label></td>
							</tr>
							
							<tr>
								<td colspan="5" id="edit">
									<button type="button" class="btn btn-lg btn-success btn-block" 
										onclick="updateAuth()">저장하기</button>
								</td>
							</tr>
						</tbody>
					</table>
				</div>
				</form>
			</div>
		</div>
	</div>
</div>

<!-- Modal-Create -->
<div class="modal fade" id="authorityInfo" tabindex="-1">
	<div class="modal-dialog">
		<form class="form-horizontal" id="authorityForm" method="post">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">×</button>
					<h4 class="modal-title" id="myModalLabel">권한 정보</h4>
				</div>

				<div class="modal-body">
					<div class="form-group">
						<label class="col-md-3 control-label">권한 이름</label>
						<div class="col-md-9">
							<input class="form-control" id="authname" name="authname">
						</div>
					</div>
					<div class="form-group">
						<label class="col-md-3 control-label">초기 전체 권한</label>
						<div class="col-md-9">
							<label class="switcher switcher-rounded switcher-lg switcher-primary switcher-blank">
								<input type="checkbox" id="authorityDefault" name="authorityDefault" checked>
								<div class="switcher-indicator">
									<div class="switcher-yes">ON</div>
									<div class="switcher-no">OFF</div>
								</div>
							</label>
						</div>
					</div>

				</div>
				<div class="modal-footer">
					<button type="submit" class="btn btn-primary">확 인</button>
					<button type="button" class="btn" data-dismiss="modal">취소</button>
					<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
				</div>
			</div>
		</form>
	</div>
</div>
<script>
	// Authority
	if(!hasRole("ROLE_AUTHORITY_EDIT")) {
		$('#delete').hide();
		$('#edit').hide();
		$('#create').hide();
		isAuthable = false;
	}
</script>
<script>
	var authnames = <%=new Gson().toJson(request.getAttribute("authnames"))%>;
	var authlist = ${authlist};
	var prename = '<%=request.getParameter("name")%>';
	
	/**
	 * Example
	 *
	 * var list = getAuth('admin');
	 * list.type -> 0 (순서대로 인덱싱되었다 가정. 지금 경우엔 0번-매장관리의 권한에 해당됨)
	 * list.authority -> 11 (앞자리는 View, 뒷자리는 삽입,수정,삭제 권한에 해당함. 0:권한없음, 1:권한있음. 지금 경우엔 11-모두 권한 있음에 해당됨)
	 */
	function getAuth(authname){
		var result = new Array;
		for(var i=0; i<authlist.length; i++) {
			var node = authlist[i];
			if(node.authname === authname) {
				result.push(node);
			}
		}
		return result;
	}
	
	function deleteAuth() {
		if($('#authnameSelector')[0].selectedIndex !== 0) {
			if(confirm('삭제 하시겠습니까?')) {
				var token = $("meta[name='_csrf']").attr("content");
				var header = $("meta[name='_csrf_header']").attr("content");
	
				$.ajax({
					type : "POST",
					beforeSend : function(request) {
						request.setRequestHeader(header, token);
					},
					url : "./authority/delete.do",
					data : {
						authname : $('#authnameSelector').val()
					},
					success : function(status) {
						if (status.code / 100 == 2) {
							location = location.origin + location.pathname;
						} else { // fail
							alert(status.message);
						}
					},
					error : function() {
						alert('ajax error');
					}
				});
			}
		} else {
			alert('권한을 선택해주세요.');
		}
	}
	
	function updateAuth() {
		if($('#authnameSelector')[0].selectedIndex !== 0) {
			if(confirm('저장 하시겠습니까?')) {
				var token = $("meta[name='_csrf']").attr("content");
				var header = $("meta[name='_csrf_header']").attr("content");
	
				var authname = $('#authnameSelector').val();
				var auth = '';
				
				var menusize = 15;
				for(var i=0; i<menusize; i++) {
					var arg1 = $('#menu'+i+'-1').is(":checked")?"1":"0"; 
					var arg2 = $('#menu'+i+'-2').is(":checked")?"1":"0";
					auth += arg1 + arg2;
				}
				
				if(auth.length === menusize*2 ) {
					$.ajax({
						type : "POST",
						beforeSend : function(request) {
							request.setRequestHeader(header, token);
						},
						url : "./authority/update.do",
						data : {
							authname : authname,
							auth : auth
						},
						success : function(status) {
							if (status.code / 100 == 2) {
								location = location.origin + location.pathname + '?name=' + authname;
							} else { // fail
								alert(status.message);
							}
						},
						error : function() {
							alert('ajax error');
						}
					});
				} else {
					alert('update error');
				}
			}
		} else {
			alert('권한을 선택해주세요.');
		}
	}
	
	function printAuth(authname) {
		var list = getAuth(authname);
		for(var i=0; i<list.length; i++) {
			var node = list[i];
			var id = 'menu'+node.type;
			$('input:checkbox[id="'+id+'-1'+'"]').prop("checked", node.authority[0]=='0'?false:true);
			$('input:checkbox[id="'+id+'-2'+'"]').prop("checked", node.authority[1]=='0'?false:true);
		}
	}
	
	function printAccess() {
		var menu = ['menu_restaurant', 'menu_porter', 'menu_member', 'menu_target', 'menu_coupon'];
		var list = [[0,1,2], [3,4], [5,6,7,8], [9,10], [11]];
		for(var i=0; i<list.length; i++) {
			var tf = true;
			for(var j=0; j<list[i].length; j++) {
				var temp1 = $('#menu'+list[i][j]+'-1').is(":checked"); 
				var temp2 = $('#menu'+list[i][j]+'-2').is(":checked");
				tf = tf && !temp1?(!temp2?true:false):false;
			}
			if(tf) {
				$('#'+menu[i]).prop("checked", false);
				$('#'+menu[i]).trigger('change');
			} else {
				$('#'+menu[i]).prop("checked", true);
			}
		}
	}
$(document).ready(function() {
	
	$("#authnameSelector").change(function() {
		if($('#authnameSelector')[0].selectedIndex === 0) {
			$('#authorityBody').hide();
		} else {
			$('#authorityBody').show();
			$('input:checkbox').prop("disabled", false);
			var authname = $('#authnameSelector').val();
			printAuth(authname);
			printAccess();
		}
	});

	$("#authorityForm").submit(function() {
		$.post('./authority/insert.do', $(this).serialize(), function(status) {
			if (status.code / 100 == 2) {
				var authname = $('#authname').val();
				$('#authorityForm')[0].reset();
				$('#authorityInfo').modal('hide');
				location = location.origin + location.pathname + '?name=' + authname;
			} else { 
				alert(status.message);
			}
		});
		return false;
	});
	
	$('#menu_restaurant').change( function(e) {
		var ischecked = e.currentTarget.checked;
		var list = [0,1,2];
		for(var i=0; i<list.length; i++) {
			var id = 'menu'+list[i];
			$('input:checkbox[id="'+id+'-1'+'"]').prop("checked", ischecked);
			$('input:checkbox[id="'+id+'-2'+'"]').prop("checked", ischecked);
			$('input:checkbox[id="'+id+'-1'+'"]').prop("disabled", !ischecked);
			$('input:checkbox[id="'+id+'-2'+'"]').prop("disabled", !ischecked);
		}
	});
	
	$('#menu_porter').change(function(e) {
		var ischecked = e.currentTarget.checked;
		var list = [3,4];
		for(var i=0; i<list.length; i++) {
			var id = 'menu'+list[i];
			$('input:checkbox[id="'+id+'-1'+'"]').prop("checked", ischecked);
			$('input:checkbox[id="'+id+'-2'+'"]').prop("checked", ischecked);
			$('input:checkbox[id="'+id+'-1'+'"]').prop("disabled", !ischecked);
			$('input:checkbox[id="'+id+'-2'+'"]').prop("disabled", !ischecked);
		}
	});
	
	$('#menu_member').change(function(e) {
		var ischecked = e.currentTarget.checked;
		var list = [5,6,7,8];
		for(var i=0; i<list.length; i++) {
			var id = 'menu'+list[i];
			$('input:checkbox[id="'+id+'-1'+'"]').prop("checked", ischecked);
			$('input:checkbox[id="'+id+'-2'+'"]').prop("checked", ischecked);
			$('input:checkbox[id="'+id+'-1'+'"]').prop("disabled", !ischecked);
			$('input:checkbox[id="'+id+'-2'+'"]').prop("disabled", !ischecked);
		}
	});
	
	$('#menu_target').change(function(e) {
		var ischecked = e.currentTarget.checked;
		var list = [9,10];
		for(var i=0; i<list.length; i++) {
			var id = 'menu'+list[i];
			$('input:checkbox[id="'+id+'-1'+'"]').prop("checked", ischecked);
			$('input:checkbox[id="'+id+'-2'+'"]').prop("checked", ischecked);
			$('input:checkbox[id="'+id+'-1'+'"]').prop("disabled", !ischecked);
			$('input:checkbox[id="'+id+'-2'+'"]').prop("disabled", !ischecked);
		}
	});
	
	$('#menu_coupon').change(function(e) {
		var ischecked = e.currentTarget.checked;
		var list = [11];
		for(var i=0; i<list.length; i++) {
			var id = 'menu'+list[i];
			$('input:checkbox[id="'+id+'-1'+'"]').prop("checked", ischecked);
			$('input:checkbox[id="'+id+'-2'+'"]').prop("checked", ischecked);
			$('input:checkbox[id="'+id+'-1'+'"]').prop("disabled", !ischecked);
			$('input:checkbox[id="'+id+'-2'+'"]').prop("disabled", !ischecked);
		}
	});
	
	$('#refresh').click(function() {
		$('input:checkbox').prop("disabled", false);
		printAuth($('#authnameSelector').val());
		printAccess();
	});
	
	if(prename != 'null' && authnames.includes(prename)) {
		$('#authorityBody').show();
		$('#authnameSelector').val(prename);
		printAuth(prename);
		printAccess();
	}
});
	
</script>