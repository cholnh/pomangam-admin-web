var editIdx;
var isAuthable = true;
var CRUD = (function() {
	/**
	 * var crud = new CRUD('settop', fields
	 * 		{
	 * 			insertbtn : function(){
	 * 				$("#regdate").val(getToday());
	 * 				$('#panel-with-switcher')[0].checked = true;
	 * 			},
	 * 			updatebtn : function(bean){
	 * 				bean.idx;
	 * 				for(var i=0; i<fields.length; i++) {
						var field = fields[i];
						$('#'+field).val(bean[field]);
					}
	 * 			}
	 * 		},
	 * 		{	// 넣지 않으면 기본 값으로 설정됨.
	 * 			table : 'table',
	 * 			modal : 'settopInfo',
	 * 			form : 'settopForm',
	 * 			export : 'export',
	 * 			insertbtn : 'create',
	 * 			updatebtn : 'edit',
	 * 			deletebtn : 'delete'
	 * 		}
	 * );
	 * 
	 * @param tablename	: db에 사용되는 table이름
	 * @param fields 	: table의 fields(속성값들)
	 * @param event		: 추가 할 이벤트
	 * @param id		: 커스텀할 아이디
	 * @returns
	 */
	function CRUD(tablename, fields, event, id) {
		if(!id) id = new Object;
		if(!id.hasOwnProperty('table')) id.table = 'table';
		if(!id.hasOwnProperty('modal')) id.modal = tablename+'Info';
		if(!id.hasOwnProperty('form')) id.form = tablename+'Form';
		if(!id.hasOwnProperty('fileexport')) id.fileexport = 'export';
		if(!id.hasOwnProperty('insertbtn')) id.insertbtn = 'create';
		if(!id.hasOwnProperty('updatebtn')) id.updatebtn = 'edit';
		if(!id.hasOwnProperty('deletebtn')) id.deletebtn = 'delete';
		
		this.tablename = tablename;
		this.fields = fields;
		this.tableid = id.table;
		this.modalid = id.modal;
		this.formid = id.form;
		this.exportid = id.fileexport;
		this.insertbtnid = id.insertbtn;
		this.updatebtnid = id.updatebtn;
		this.deletebtnid = id.deletebtn;
		
		// insert button event
		$('#'+this.insertbtnid).off('click').on('click',
			function(e) {
				$('#'+id.form)[0].reset();
				$(':submit').attr('id','insert');
				$('#idx').val(null);
				if(event && event.insertbtn) {
					event.insertbtn();
				} else {
					$('#'+id.modal).modal({backdrop: 'static'});
					$('#'+id.modal).modal('show');
				}
			}
		);
		
		// update button event
		$('#'+this.updatebtnid).off('click').on('click', 
			function(e) {
				var selections = $('#'+id.table).bootstrapTable('getSelections');
				
				if (selections.length > 1)
					alert('한 개만 선택하세요.');
				else if (selections.length == 0)
					alert('수정할 데이터를 선택하세요.');
				else {
					var bean = selections[0];
					setModalInfo(bean);
					$(':submit').attr('id','update');
					if(event && event.updatebtn) {
						event.updatebtn(bean);
					} else {
						$('#'+id.modal).modal({backdrop: 'static'});
						$('#'+id.modal).modal('show');
					}
				}
			}
		);
		$('#'+this.tableid).off('dbl-click-row.bs.table').on('dbl-click-row.bs.table',
			function(e, row, $element) {
				if(isAuthable) {
					setModalInfo(row);
					$(':submit').attr('id','update');
					if(event && event.updatebtn) {
						event.updatebtn(row);
					} else {
						$('#'+id.modal).modal({backdrop: 'static'});
						$('#'+id.modal).modal('show');
					}
				}
			}
		);
		
		// delete button event
		$('#delete').off('click').on('click', 
			function(e) {
				var selections = $('#'+id.table).bootstrapTable('getSelections');
				if(selections.length <= 0) {
					alert('삭제할 데이터를 선택하세요.');
					return;
				}

				var selectedIdxes = '';
				for(var index = 0 ; index < selections.length ; index++){
					selectedIdxes += selections[index].idx + ',';
				}
				
				if(confirm('삭제 하시겠습니까?')) {
					if(event && event.deletecheck) {
						if(event.deletecheck()) {
							return false;
						}
					}
					ajax('./'+tablename+'/delete.do', 
						{idxes: selectedIdxes},
						true,
						function(status) {
							if (status.code / 100 == 2) {
								if(event && event.deletesbm) {
									event.deletesbm();
								} else {
									$('#'+id.table).bootstrapTable('refresh');
								}
							} else {
								alert(status.message);
							}
						}
					);
				}
			}
		);
		
		// submit event
		$("#"+this.formid).submit(function() {
			if($(':submit').attr('id') == 'insert'){
				if(event && event.insertcheck) {
					if(event.insertcheck()) {
						return false;
					}
				}
				if(event && event.insertalter) {
					event.insertalter();
				} else {
					$.post('./'+tablename+'/insert.do', $(this).serialize(), function(status) {
						if (status.code / 100 == 2) {
							if(event && event.insertsbm) {
								event.insertsbm(status.message);
							} else {
								$('#'+id.modal).modal('hide');
								$('#'+id.table).bootstrapTable('refresh');
							}
							$('#'+id.form)[0].reset();
						} else {
							alert(status.message);
						}
					});
					return false;
				}
			}
			else if($(':submit').attr('id') == 'update'){
				if(event && event.updatecheck) {
					if(event.updatecheck()) {
						return false;
					}
				}
				if(event && event.updatealter) {
					event.updatealter(editIdx);
				} else {
					$.post('./'+tablename+'/update.do', $(this).serialize() + editIdx, function(status) {
						if (status.code / 100 == 2) {
							if(event && event.updatesbm) {
								event.updatesbm(editIdx.substring(editIdx.lastIndexOf("=")+1));
							} else {
								$('#'+id.modal).modal('hide');
								$('#'+id.table).bootstrapTable('refresh');
							}
							$('#'+id.form)[0].reset();
						} else {
							alert(status.message);
						}
					});
					return false;
				}
			}
		});
		
		// export event
		$('#'+this.exportid).off('click').on('click', function(e) {
			$('#'+id.table).bootstrapTable('togglePagination');
			$('#'+id.table).tableExport({
			      type: 'excel'
	        });
		});
	}
	
	function ajax(url, data, async, success, error) {
		var token = $("meta[name='_csrf']").attr("content");
		var header = $("meta[name='_csrf_header']").attr("content");
		if(!token || !header) {
			alert('csrf token을 찾을 수 없습니다.');
			return;
		}
		$.ajax({
			type : "POST",
			beforeSend : function(request) {
				request.setRequestHeader(header, token);
			},
			url : url,
			data : data,
			async: async,
			success : success ? success : 
				function(status) {
					if (status.code / 100 == 2) {
						location = location.origin + location.pathname;
					} else { // fail
						alert(status.message);
					}
				},
			error : error ? error : 
				function() {
					alert('ajax error');
				}
		});
	};
	
	function setModalInfo(row) {
		editIdx = '&idx=' + row.idx;
		for(var i=0; i<fields.length; i++) {
			var field = fields[i];
			$('#'+field).val(row[field]);
		}
		$('#idx').val(row.idx);
	}
	
	// Not Used
	CRUD.prototype.init = function() {
		ajax('./'+this.tablename+'/getfields.do', null, false, 
			function(status) {
				if (status.code / 100 == 2) {
					crudfileds = status.message;
					console.log('successfully CRUD has been initialized');
				} else { // fail
					alert(status.message);
				}
			}
		);
	};
	
	return CRUD;
})();