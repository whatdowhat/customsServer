<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="../layout/resources.jsp" %>

<body>
<!-- BEGIN #loader -->
<div id="loaderCustom" class="app-loader">
  <div id="imgLoading" class="imgLoading"></div>
</div>
<!-- END #loader -->
 
<!-- BEGIN #app -->
<!-- <div id="app" class="app app-header-fixed app-sidebar-fixed "> -->
<div id="app" class="app app-sidebar-minified">

  <!-- BEGIN #header -->
  <div id="header" class="app-header">
  	<%@ include file="../layout/header.jsp" %>

  </div>
  <!-- BEGIN #header -->
  <!-- BEGIN #sidebar -->
	<%@ include file="../layout/nav.jsp" %>
  <!-- END #sidebar -->
  
  <!-- BEGIN #content -->
		<div id="content" class="app-content">
			<!-- BEGIN breadcrumb -->
<!-- 			<ol class="breadcrumb float-xl-end">
				<li class="breadcrumb-item"><a href="javascript:;">Home</a></li>
				<li class="breadcrumb-item"><a href="javascript:;">Tables</a></li>
				<li class="breadcrumb-item active">Basic Tables</li>
			</ol> -->
			<!-- END breadcrumb -->
			<!-- BEGIN page-header -->
			<h1 class="page-header">웹 관리자 <!-- <small>header small text goes here...</small> --></h1>
			
			<!-- END page-header -->
			<!-- BEGIN row -->
	
			
			
        <!-- Begin page -->
                        <div class="row">
                            <div class="col-12">
                                <div class="card">
                                    <div class="card-body">

	            <!-- ============================================================== -->
	            <!-- Start right Content here -->
	            <!-- ============================================================== -->
						
                                        <div class="mb-3 row">
                                            <div class="col-md-12">
	 											<div class="mb-3">
	                                                <!-- <input type="file" name="file" id="file" accept="application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"> -->
	                                                <span class="btn btn-primary fileinput-button me-1">
														<i class="fa fa-fw fa-plus"></i>
														<span>파일선택</span>
														<input type="text" onchange="changedata()">
														<input id="file" type="file" name="file" >
													</span>
	                                                <button type="button" class="btn btn-primary" id="clickupload" onclick="upload()">엑셀 데이터 읽기</button>
	                                            </div>
                                            </div>
 											<div class="col-md-12" id="uploadType"style="display: none;">
	 											<div class="mb-3">
	                                                <!-- inline -->
													<div class="form-check form-check-inline">
														<input class="form-check-input" type="radio" id="inlineRadio1" name="inlineRadio" checked="checked" value="NEW"><label for="inlineRadio1">데이터 추가</label> 
													</div>
													<div class="form-check form-check-inline">
														<input class="form-check-input" type="radio" id="inlineRadio2" name="inlineRadio" value="OVERRIDE"><label for="inlineRadio2">데이터 덮어쓰기</label> 
													</div>
	                                                <!-- <input type="file" class="filestyle" data-buttonname="btn-secondary"> -->
	                                            </div>
                                            </div>
 											<div class="col-md-12" id="uploadCommit"style="display: none;">
	 											<div class="mb-3">
													<button type="button" class="btn btn-primary waves-effect waves-light" id="commit" >엑셀 데이터 반영</button>
	                                            </div>
                                            </div>
										</div>
                                       
                          
                        <!-- end row -->
                        
                                        <div class="table-responsive">
	                                        <input type="text" name="searchColumText" id="searchColumText" placeholder="검색어를 입력해주세요.">
												<select name="searchColum" id="searchColum">
												    <option value="eduDt">교육일자</option>
												    <option value="startHhmm">시작시간</option>
												    <option value="endHhmm">종료시간</option>
												    <option value="memberProject">프로젝트명</option>
												    
												</select>
				 								<button id="searchBtn" class="btn btn-primary">검색</button>
				 								<button id="showAll" class="btn btn-primary" style="display: none;">전체 데이터 보기</button>
				 								<!-- <button type="button" class="btn btn-pink waves-effect waves-light" id="commitEdit" style="float: right;" >수정사항 반영</button> -->
				 								<p id="allData"></p>
	                                        <div style="margin-bottom: 10px">
	                                        <!-- table start -->
		                                        <table id="datatable"    class="table table-bordered nowrap" style="border-collapse: collapse; border-spacing: 0; width: 100%;">
		                                            <thead>
		                                            <tr>
		                                            	<th>순서</th>
		                                            	<th>업로드가능 여부</th>
		                                                <th>교육날짜</th>
		                                                <th>시작시간</th>
		                                                <th>종료시간</th>
		                                                <th>프로젝트명</th>
		                                                <th>멘토 팀명</th>
		                                                <th>멘토 이름</th>
		                                                <th>멘토 이메일</th>
		                                                <th>멘토 핸드폰</th>
		                                                <th>리더여부</th>
		                                                <th>기관명</th>
		                                                <th>매칭된 학생</th>
		                                                <th>출석여부</th>
		                                                <th>기관담당자 이름</th>
		                                                <th>기관담당자 이메일</th>
		                                                <th>기관담당자 연락처</th>
		                                                <th>링크</th>
		                                                <th style="display: none;">startDt</th>
		                                                <th style="display: none;">endDt</th>
		                                            </tr>
		                                            </thead>
		                                        </table>
	                                        </div>
                                    
                        				</div>
		            <!-- ============================================================== -->
		            <!-- End right Content here -->
		            <!-- ============================================================== -->

                   				 </div> 
                         	 </div>
                        </div>
                                
                   </div>
		</div>    

  <!-- END #content -->
    
  <!-- BEGIN scroll-top-btn -->
  <a href="javascript:;" class="btn btn-icon btn-circle btn-success btn-scroll-to-top" 
    data-toggle="scroll-to-top">
    <i class="fa fa-angle-up"></i>
  </a>
  <!-- END scroll-top-btn -->
</div>
<!-- END #app -->

</body>

<script type="text/javascript">
var original_target ={};
var _editing = false;
var _editingRow;
var mentorNames = [];
var studentNames = [];
var projectNames = [];
var teamNames = [];


var targetFile;


var _table1; 
var controller;

var _idx = {
		column : -1,
		columnVisible:-1,
		row:-1
}
var _cellValue;
var _originalValue ='*';
var _targetSeq = 0;
var _targetCell = {};
var _targetColums = [2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17];

function initEdit(){
	_idx = {
			column : -1,
			columnVisible:-1,
			row:-1
	}
}


function isTarget(target){
	
	const result = _targetColums.filter((number, index, source) =>{
		return number == target;
	});
	return result > 0;
}



$("#commit").on("click",function(event){
	
	try{
		if(_targetCell != undefined){
			if(_targetCell.data().indexOf('<input type="text"')>-1){
				alert('수정 컬럼이 활성화 되어 있습니다. 확인 해주세요.');
				return false;
				
			}		
		}
		
	}catch(e){
		console.log('default commit')
	}
	
	
	/* if(_originalValue =='*'){
		_targetCell.data(_originalValue);	
	}else if(_originalValue =='*'){
		//_targetCell.data(_originalValue);
	}else{
		
	} */
		initEdit();
	var table = $('#datatable').DataTable();

	var list =[];
	
	table.rows().every( function () {
	    var d = this.data();
	 	list.push(d);
	    console.dir(d);
	    console.dir(list);
	});
	
	var datas = JSON.stringify(list);
	var options = '';
	$("input:radio[name='inlineRadio']").map( (index,item) =>{
		if(item.checked){
			options = item.value;
		}
	});
	
/* 	
    $.ajax({
    	url: '/excelUpload/data02?${_csrf.parameterName}=${_csrf.token}',
        data:{data:data},
        type: 'post',
        success:function(data){ //"inedx3"
         	
       	 console.log('success');
       	 console.dir(data);
       	 if(data.result){
       		alert(data.processCnt+"건 반영완료.");	 
       	 }else{
       		alert(data.processCnt+"/"+data.listSize+"건 일부 반영");	 
       	 }
       	
        
        },	//success끝 
	     error : function(error) {
	    	 console.log('error');
	    	 console.dir(error);
	    	 alert("오류가 발생했습니다. 관리자에게 문의하세요.");
	     },
	     complete : function() {
	        // alert("complete!");    
	        console.log('complete called');
	     }
    });	 */
	
	$.ajax.PromisePost('/excelUpload/data02?${_csrf.parameterName}=${_csrf.token}',{data:datas,option:options})
	.then(commitData);
});


function commitData(result,status){
	return new Promise(function(resolve,reject){
		
       	 if(result.result){
       		alert(result.processCnt+"건 반영완료.");	 
       	 }else{
       		alert(result.processCnt+"/"+data.listSize+"건 일부 반영");	 
       	 }
       	
       	 
	});
}




function upload(){
				
	targetFile = $("#file")[0].files[0];
	var formData = new FormData();
	var file = document.getElementById("file");
	formData.append("file", file.files[0]);

	
	$.ajax.PromisePostForm('/excelUpload/data01?${_csrf.parameterName}=${_csrf.token}',formData)
	.then(readEexcel);
		
		
}


function readEexcel(result,status){
	return new Promise(function(resolve,reject){
		//console.dir(result);
		//console.dir(status);
		if(result.data.length>0){
			//insert type option 노출
			$("#uploadType").show();
			$("#uploadCommit").show();
			
		}
		
        
       	//수정할 이름 저장.
        studentNames = [];
        mentorNames = [];
        projectNames = [];
        teamNames = [];
        //console.log('data:::',data.data);
        $.each(result.data,function(index,item){
        	studentNames.push(item.matchingNm);
        	mentorNames.push(item.memberNm);
        	projectNames.push(item.memberProject);
        	teamNames.push(item.memberTeam);
        })
        
		 _table1= $('#datatable').DataTable({
			 dom: 'Brtip',
			paging : false,
	    	data:result.data,
	    	searching: true,
	    	"dataSrc": "",
	    	"pagingType" : "full_numbers",
	    	"processing" : true,
	    	"destroy" : true,
	    	"buttons": [
                'copy', 'excel', 'csv', 'print'
            ],
	    	"columns" : [ 
	        	
	        	{ data: 'lineNumber'},
/* 	        	{ data: 'uploadAvailable' ,render:function(data, type, full, meta){
	        		if(data == '업로드가능'){
	        			return '<div  style="color: green" >'+data+'</div>';	
	        		}else{
	        			return '<div  style="color: red" >'+data+'</div>';
	        		}
				}}, */
	        	{ data: 'seq',render:function(data, type, full, meta){
	        		return '<button class="btn btn-info btn-lg" value="'+data+'" id="btn_'+data+'" onclick="editColumn('+meta.row+')" />수정하기</button>'; 
				}},
	        	{ data: 'yyyyMMdd'},
			    { data: 'startHhmm' },
			    { data: 'endHhmm' },
			    { data: 'memberProject' },
			    { data: 'memberTeam' },
			    { data: 'memberNm' },
			    { data: 'memberEmail' },
			    { data: 'memberPh' },
			    { data: 'memberLeaderYn' },
			    { data: 'govNm' },
			    { data: 'matchingNm' },
			    { data: 'attendYn' },
			    { data: 'govPersonNm' },
			    { data: 'govPersonEmail' },
			    { data: 'govPersonPh' },
			    { data: 'linkUrl' },
			    
			    { data: 'startDtString' },
			    { data: 'endDtString' },
			    
	       	],
		        'columnDefs': [{
	            'targets': 18,
	            'visible' : false,
	            'searchable': false,
	            'orderable': false,
	            'className': 'dt-body-center',
	            'render': function (data, type, full, meta){
	                return data;
	            }
	         },
	         {
		            'targets': 19,
		            'visible' : false,
		            'searchable': false,
		            'orderable': false,
		            'className': 'dt-body-center',
		            'render': function (data, type, full, meta){
		                return data;
		            }
		         },

	       ] 
	       
	    });
		
		 $('#searchBtn').on('click', function () {
		    	 
		    	 var target = $("#searchColum option:selected").val();
		    	 var targetText = $("#searchColumText").val();
		    	 if(target == 'startHhmm'){
		    		 _table1
		             .columns([3])
		             .search(targetText)
		             .draw(); 
		    	 }else if(target == 'endHhmm'){
		    		 _table1
		             .columns([4])
		             .search(targetText)
		             .draw(); 
		    	 }else if(target == 'memberProject'){
		    		 _table1
		             .columns([5])
		             .search(targetText)
		             .draw(); 
		    		 
		    	 }else if(target == 'eduDt'){
					 _table1
				        .columns([1])
				        .search(targetText)
				        .draw(); 
						 
					 }
		    	 
		     });
		 
	});
}

$("#showAll").on("click",function(event){
	var table = $('#datatable').DataTable();

	var list =[];
 	table.rows().every( function () {
	    var d = this.data();
	 	list.push(d);
	    //console.dir(d);
	    //console.dir(list);
	}); 
	
 	var data = JSON.stringify(list);
	$("#allData").empty();
	$("#allData").append(data);
});

$("#commitEdit").on("click",function(event){
	var table = $('#datatable').DataTable();
	
	var list =[];
	
	
	table.rows().every( function () {
	    var d = this.data();
	 	list.push(d);
	    console.dir(d);
	    console.dir(list);
	});
	
	var dd = JSON.stringify(list);
	
    $.ajax({
    	url: '/admin/viewData/edit?${_csrf.parameterName}=${_csrf.token}',
        data:{dd:dd},
        type: 'post',
        success:function(data){ //"inedx3"
         	
       	 console.log('success');
       	 console.dir(data);
       	alert("수정사항이 반영되었습니다.");
        
        },	//success끝 
	     error : function(error) {
	    	 console.log('error');
	    	 console.dir(error);
	    	 alert("수정사항의 형식을 확인해주세요(ex:교육날짜:YYYY-MM-DD, 시작 및 종료시간 HH:mm)");
	     },
	     complete : function() {
	        // alert("complete!");    
	        console.log('complete called');
	     }
    });	

	
});


function editColumn(param){
	
	//$.extend(true,original_target,);
	//$.extend(true,_table1.row(param).data(),original_target);
	
	
	if(_editing == false){
		_editing = true;
		_editingRow = param;
		$.extend(true,original_target,_table1.row(param).data());    
	    
	    var change_target = _table1.row(param).cell(param,2);
	    change_target.data('<input type="text" id="yyyyMMdd" name="yyyyMMdd" value="'+change_target.data()+'">');
	    
	    var change_target = _table1.row(param).cell(param,3);
	    change_target.data('<input type="text" id="startHhmm" name="startHhmm" value="'+change_target.data()+'">');
	    
	    var change_target = _table1.row(param).cell(param,4);
	    change_target.data('<input type="text" id="endHhmm" name="endHhmm" value="'+change_target.data()+'">');
	    
	    var change_target = _table1.row(param).cell(param,5);
	    //change_target.data('<input type="text" id="memberProject" name="memberProject" value="'+change_target.data()+'">');
	    var value = change_target.data();
	    var html ="";
	    html += '<select name="memberProject" id="memberProject">';
	    $.each(projectNames,function(index,item){
	    	html += '<option value="'+item+'">'+item+'</option>';	
	    })
	    html += '<option value="WRITE">수기입력</option>';
	    html += '</select>';
		
		change_target.data(html);
		$("#memberProject").val(value).prop("selected", true);
		$("#memberProject").change(function(e){

			if(this.value == 'WRITE'){
				var valueText = prompt('수기 작성 추가. 추가 후, 선택해주세요.');
				if(valueText!=null){
					if(valueText != ''){
						$("#memberProject").append('<option value="'+valueText+'">'+valueText+'</option>')	
					}	
				}
				
					
				
			}
			
		});
	    
	    var change_target = _table1.row(param).cell(param,6);
	    //change_target.data('<input type="text" id="memberTeam" name="memberTeam" value="'+change_target.data()+'">');
	    var value = change_target.data();
	    var html ="";
	    html += '<select name="memberTeam" id="memberTeam">';
	    $.each(teamNames,function(index,item){
	    	html += '<option value="'+item+'">'+item+'</option>';	
	    })
	    html += '<option value="WRITE">수기입력</option>';
	    html += '</select>';
		
		change_target.data(html);
		$("#memberTeam").val(value).prop("selected", true);
		$("#memberTeam").change(function(e){

			if(this.value == 'WRITE'){
				var valueText = prompt('수기 작성 추가. 추가 후, 선택해주세요.');
				if(valueText!=null){
					if(valueText != ''){
						$("#memberTeam").append('<option value="'+valueText+'">'+valueText+'</option>')	
					}	
				}
				
					
				
			}
			
		});
	    
	    var change_target = _table1.row(param).cell(param,7);
	    //change_target.data('<input type="text" id="memberNm" name="memberNm" value="'+change_target.data()+'">');
	    var value = change_target.data();
	    var html ="";
	    html += '<select name="memberNm" id="memberNm">';
	    $.each(mentorNames,function(index,item){
	    	html += '<option value="'+item+'">'+item+'</option>';	
	    })
	    html += '<option value="WRITE">수기입력</option>';
	    html += '</select>';
		
		change_target.data(html);
		$("#memberNm").val(value).prop("selected", true);
		$("#memberNm").change(function(e){

			if(this.value == 'WRITE'){
				var valueText = prompt('수기 작성 추가. 추가 후, 선택해주세요.');
				if(valueText!=null){
					if(valueText != ''){
						$("#memberNm").append('<option value="'+valueText+'">'+valueText+'</option>')	
					}	
				}
				
					
				
			}
			
		});

	    var change_target = _table1.row(param).cell(param,8);
	    change_target.data('<input type="text" id="memberEmail" name="memberEmail" value="'+change_target.data()+'">');
	    
	    var change_target = _table1.row(param).cell(param,9);
	   
	    change_target.data('<input type="text" id="memberPh" name="memberPh" value="'+change_target.data()+'">');
	    
	    var change_target = _table1.row(param).cell(param,10);
	    var value = change_target.data();
	    var html ="";
	    html += '<select name="memberLeaderYn" id="memberLeaderYn">';
	    html += '<option value="리더">리더</option>';
	    html += '<option value="">공란</option>';
	    html += '</select>';
		
		change_target.data(html);
		$("#memberLeaderYn").val(value).prop("selected", true);
	    
	    var change_target = _table1.row(param).cell(param,11);
	    change_target.data('<input type="text" id="govNm" name="govNm" value="'+change_target.data()+'">');
	    
	    var change_target = _table1.row(param).cell(param,12);
	    //change_target.data('<input type="text" id="matchingNm" name="matchingNm" value="'+change_target.data()+'">');
	    
	    var value = change_target.data();
	    var html ="";
	    html += '<select name="matchingNm" id="matchingNm">';
	    $.each(studentNames,function(index,item){
	    	html += '<option value="'+item+'">'+item+'</option>';	
	    })
	    html += '<option value="WRITE">수기입력</option>';
	    html += '</select>';
		
		change_target.data(html);
		$("#matchingNm").val(value).prop("selected", true);
		$("#matchingNm").change(function(e){

			if(this.value == 'WRITE'){
				var valueText = prompt('수기 작성 추가. 추가 후, 선택해주세요.');
				if(valueText!=null){
					if(valueText != ''){
						$("#matchingNm").append('<option value="'+valueText+'">'+valueText+'</option>')	
					}	
				}
				
					
				
			}
			
		});
	    
	    
	    var change_target = _table1.row(param).cell(param,13);
	    //change_target.data('<input type="text" id="attendYn" name="attendYn" value="'+change_target.data()+'">');
	    var value = change_target.data();
	    var html ="";
	    html += '<select name="attendYn" id="attendYn">';
	    html += '<option value="출석">출석</option>';
    	html += '<option value="">결석</option>';
	    html += '</select>';
		change_target.data(html);
		$("#attendYn").val(value).prop("selected", true);
		
	    var change_target = _table1.row(param).cell(param,14);
	    change_target.data('<input type="text" id="govPersonNm" name="govPersonNm" value="'+change_target.data()+'">');
	    
	    var change_target = _table1.row(param).cell(param,15);
	    change_target.data('<input type="text" id="govPersonEmail" name="govPersonEmail" value="'+change_target.data()+'">');
	    
	    var change_target = _table1.row(param).cell(param,16);
	    change_target.data('<input type="text" id="govPersonPh" name="govPersonPh" value="'+change_target.data()+'">');
	    
	    var change_target = _table1.row(param).cell(param,17);
	    change_target.data('<input type="text" id="linkUrl" name="linkUrl" value="'+change_target.data()+'">'); 
	    
	    var change_target = _table1.row(param).cell(param,18);
	    //change_target.data('<input type="text" id="attendYn" name="attendYn" value="'+change_target.data()+'">');
	    var value = change_target.data();
	    var html ="";
	    html += '<select name="stAttendYn" id="stAttendYn">';
	    html += '<option value="출석">출석</option>';
    	html += '<option value="">결석</option>';
	    html += '</select>';
		change_target.data(html);
		$("#stAttendYn").val(value).prop("selected", true);
	    
	    var seq =original_target.seq;
	    var btnid = "btn_"+seq;
	    
	    $("#"+btnid)[0].innerText = '수정완료';
	}else{
		
		if(_editingRow != null && _editingRow != param ){
			alert('다른 곳을 수정중입니다. 수정완료 후 수정가능합니다.');
			return false;
		}else{
			var edit_target = {};
			
		    var change_target = _table1.row(param).cell(param,2);
		    change_target.data($("#yyyyMMdd").val());
		    edit_target.yyyyMMdd = change_target.data();
		    
		    var change_target = _table1.row(param).cell(param,3);
		    change_target.data($("#startHhmm").val());
		    edit_target.startHhmm = change_target.data();
		    
		    var change_target = _table1.row(param).cell(param,4);
		    change_target.data($("#endHhmm").val());
		    edit_target.endHhmm = change_target.data();
		    
		    var change_target = _table1.row(param).cell(param,5);
		    change_target.data($("#memberProject").val());
		    edit_target.memberProject = change_target.data();
		    
		    var change_target = _table1.row(param).cell(param,6);
		    change_target.data($("#memberTeam").val());
		    edit_target.memberTeam = change_target.data();
		    
		    var change_target = _table1.row(param).cell(param,7);
		    change_target.data($("#memberNm").val());
		    edit_target.memberNm = change_target.data();
		    
		    var change_target = _table1.row(param).cell(param,8);
		    change_target.data($("#memberEmail").val());
		    edit_target.memberEmail = change_target.data();
		    
		    var change_target = _table1.row(param).cell(param,9);
		    change_target.data($("#memberPh").val());
		    edit_target.memberPh = change_target.data();
		    
		    var change_target = _table1.row(param).cell(param,10);
		    change_target.data($("#memberLeaderYn").val());
		    edit_target.memberLeaderYn = change_target.data();
		    
		    var change_target = _table1.row(param).cell(param,11);
		    change_target.data($("#govNm").val());
		    edit_target.govNm = change_target.data();
		    
		    var change_target = _table1.row(param).cell(param,12);
		    change_target.data($("#matchingNm").val());
		    edit_target.matchingNm = change_target.data();
		    
		    var change_target = _table1.row(param).cell(param,13);
		    change_target.data($("#attendYn").val());
		    edit_target.attendYn = change_target.data();
		    
		    var change_target = _table1.row(param).cell(param,14);
		    change_target.data($("#govPersonNm").val());
		    edit_target.govPersonNm = change_target.data();
		    
		    var change_target = _table1.row(param).cell(param,15);
		    change_target.data($("#govPersonEmail").val());
		    edit_target.govPersonEmail = change_target.data();
		    
		    var change_target = _table1.row(param).cell(param,16);
		    change_target.data($("#govPersonPh").val());
		    edit_target.govPersonPh = change_target.data();
		    
		    var change_target = _table1.row(param).cell(param,17);
		    change_target.data($("#linkUrl").val());
		    edit_target.linkUrl = change_target.data();
		    
		    var change_target = _table1.row(param).cell(param,18);
		    change_target.data($("#stAttendYn").val());
		    edit_target.stAttendYn = change_target.data();
		    
		    var seq =original_target.seq;
		    var btnid = "btn_"+seq;
		    $("#"+btnid)[0].innerText = '수정하기';
			
		    _editing = false;
			_editingRow = null;
			

			delete original_target.regDt;
			delete original_target.startDt;
			delete original_target.endDt;
			delete original_target.editDt;
			
		}
		
	}
	
    
    
}


</script>

