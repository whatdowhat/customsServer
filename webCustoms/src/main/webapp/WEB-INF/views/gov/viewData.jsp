<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 
<%@ include file="../layout/resources.jsp" %>
	<link href="/assets/plugins/blueimp-file-upload/css/jquery.fileupload.css" rel="stylesheet" />
	<link href="/assets/plugins/blueimp-file-upload/css/jquery.fileupload-ui.css" rel="stylesheet" />

	<link href="/assets/plugins/jstree/dist/themes/default/style.min.css" rel="stylesheet" />
	<script src="/assets/plugins/jstree/dist/jstree.min.js"></script>
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
			<h1 class="page-header">ViewData <!-- <small>header small text goes here...</small> --></h1>
			
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
	                                                <!-- <input id="file" type="file" name="file" > -->
	                                                
	                                            </div>
                                            </div>
										</div>
                                        <div class="mb-3 row">
                                            <div class="col-md-3">
	 											<div class="mb-3">
	                                              <div id="jstree-default2"></div>
	                                                
	                                            </div>
                                            </div>
										</div>

 
 <br>
 <br>
 <br>
 <br>
 <br>
 								<div class="table-responsive">
	 							<div class="mb-3">
									<div class="form-check form-check-inline">
										<input class="form-check-input" type="radio" id="inlineRadio1" name="inlineRadio" checked="checked" value="Y"><label for="inlineRadio1">기간 검색 적용</label> 
									</div>
									<div class="form-check form-check-inline">
										<input class="form-check-input" type="radio" id="inlineRadio2" name="inlineRadio" value="N"><label for="inlineRadio2">기간 검색 미적용</label> 
									</div>
	                            </div>
 								<input type="date" id="calendarSt" onchange="calendarSt(event);" style="height: 32px" >
 								<input type="date" id="calendarEd" onchange="calendarEd(event);" style="height: 32px">
 								<input type="text" name="searchColumText" id="searchColumText" placeholder="검색어를 입력해주세요.">
								<select name="searchColum" id="searchColum">
									<option value="eduDt">교육일자</option>
								    <option value="startHhmm">시작시간</option>
								    <option value="endHhmm">종료시간</option>
								    
								    
								</select>
 								<button id="searchBtn" class="btn btn-primary">검색</button>
 								<button id="showAll" class="btn btn-primary" style="display: none;">전체 데이터 보기</button>
 								
 								<p id="allData"></p>
	                                        <div style="margin-bottom: 10px">
	                                        <!-- table start -->
		                                        <table id="datatable"    class="table table-bordered nowrap" style="border-collapse: collapse; border-spacing: 0; width: 100%;">
		                                            <thead>
		                                            <tr>
		                                              <!--  <th><input type="checkbox" name="allcheck" onchange="allCheck()" > select </th> -->
		                                              	<th>수정</th>
		                                                <th>교육날짜</th>
		                                                <th>시작시간</th>
		                                                <th>종료시간</th>
		                                                <th>프로젝트명</th>
		                                                <th>멘토 팀명</th>
		                                                <th style="color: red;" >멘토 이름</th>
		                                                <th style="color: red;" >멘토 출석여부</th>
		                                                <th>멘토 이메일</th>
		                                                <th>기관명</th>
		                                                <th>기관담당자 이름</th>
		                                                <th>링크</th>
		                                                <th style="color: red;" >매칭된 학생</th>
		                                                <th style="color: red;" >학생 출석여부</th>
		                                                <th>확정여부</th>
		                                                <th>확정</th>
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





<!-- html -->


<script>

var original_target ={};
var _clickTreeValues;
var _table1; 
var controller;
var _isEdit = false;


var _editing = false;
var _editingRow;

var mentorNames = [];
var studentNames = [];


var _clickTreeValues;
var _table1; 
var controller;


var studentNames = [];

var _idx = {
		column : -1,
		columnVisible:-1,
		row:-1
}
var _cellValue;
var _originalValue;
var _targetSeq = 0;
var _targetCell = {};
var _targetColums = [8,9];

//기간 검색 미적용 클릭 시
$("input:radio[name='inlineRadio']").change(function() {
    if ($(this).val() === 'N') {
    	var data = _clickTreeValues;
    	if (typeof data=="undefined"){
     		  alert("팀명을 먼저 선택해주세요");
     	  }else{
    	data.searchSt = $("#calendarSt").val();
    	data.searchEd = $("#calendarEd").val();
    	
    	$("input:radio[name='inlineRadio']").map( (index,item) =>{
    		if(item.checked){
    			data.searchStEdYn = item.value;
    		}
    	});
    	
    	 
 	   		makeTeamClickDt(data);
 	   	
     	 }
    }
    
  });
//기간 검색 허용버튼 클릭 시
$("input:radio[name='inlineRadio']").change(function() {
    if ($(this).val() === 'Y') {
    	var data = _clickTreeValues;
    	 if (typeof data=="undefined"){
   		  alert("팀명을 먼저 선택해주세요");
   	  }else{
   		  
   	  
    	data.searchSt = $("#calendarSt").val();
    	data.searchEd = $("#calendarEd").val();
    	
    	$("input:radio[name='inlineRadio']").map( (index,item) =>{
    		if(item.checked){
    			data.searchStEdYn = item.value;
    		}
    	});
    	
    	
 	   		makeTeamClickDt(data);
 	   	
   	  }
    }
    
  });

function calendarSt(e){
	/* alert(e.target.value); */
	  var data = _clickTreeValues;
	  if (typeof data=="undefined"){
		  alert("프로젝트명이나 팀명을 먼저 선택해주세요");
	  }else{
		  data.searchSt =  e.target.value;
		  data.searchEd = $("#calendarEd").val();
			
			$("input:radio[name='inlineRadio']").map( (index,item) =>{
				if(item.checked){
					data.searchStEdYn = item.value;
				}
			});
			
	   	/* console.log("clickvalue");
	   	console.log(_clickTreeValues); */
	   	
	   		makeTeamClickDt(data);
	   	
	 }
}

function calendarEd(e){
	  
	  var data = _clickTreeValues;
	  if (typeof data=="undefined"){
		  alert("프로젝트명이나 팀명을 먼저 선택해주세요");
	  }else{
		  data.searchSt = $("#calendarSt").val();
		  data.searchEd = e.target.value;
			
			$("input:radio[name='inlineRadio']").map( (index,item) =>{
				if(item.checked){
					data.searchStEdYn = item.value;
				}
			});
	     	
	     	
	     		makeTeamClickDt(data);
	     	
		}
  }

function initEdit(){
	_idx = {
			column : -1,
			columnVisible:-1,
			row:-1
	}
}


function isTarget(target){
	
	const result = _targetColums.filter((number, index, source) =>{
		//console.log('istarget',number);
		//console.log('istarget',target);
		return number == target;
	});
	return result > 0;
}


/* function deleteSeq(){
	
	var checkedSeq = []; 
	$("input:checkbox[name='seqCheck']").map(function(index,item){
		if(item.checked == true){
			checkedSeq.push(item.value);	
		}
		
	})
	
	if(checkedSeq.length == 0){
		alert("체크박스 한개 이상을 선택해주세요.");
		return false;
	}
	console.log('deleteSeq',checkedSeq);
	
} */

/* function allCheck(){
	$("input:checkbox[name='seqCheck']").map(function(index,item){
			
		if($("input:checkbox[name='allcheck']")[0].checked == true){
			item.checked = true;
		}else{
			item.checked = false;
		}
	})
} */



$("#jstree-default2").click(function() {

	var click =	$('#jstree-default2').jstree('get_selected',true);
	_editing = false;
	_editingRow = null;
	var data = {};
	data.searchSt = $("#calendarSt").val();
	data.searchEd = $("#calendarEd").val();
	
	$("input:radio[name='inlineRadio']").map( (index,item) =>{
		if(item.checked){
			data.searchStEdYn = item.value;
		}
	});
	
	
	if(click[0].parent!='#'){
	var parentId="#"+click[0].parent+"_anchor";
	var parentValue=$(parentId)[0].innerText;
	var childValue=click[0].text;
	
	
	data.memberProject=parentValue
	data.memberTeam=childValue;
	
	
		
	
	_clickTreeValues = data;
	callDataClickTeam(data);
	
	
	}
});

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


$(function(){
	
	$("#jstree-default").jstree({
		"plugins": ["types"],
		"core": {
			"themes": {
				"responsive": false
			}            
		},
		"types": {
			"default": {
				"icon": "fa fa-folder text-warning fa-lg"
			},
			"file": {
				"icon": "fa fa-file text-inverse fa-lg"
			}
		}
	});

	$("#jstree-default").on("select_node.jstree", function(e,data) { 
		var link = $("#" + data.selected).find("a");
		console.log('selected',data);
		if (link.attr("href") != "#" && link.attr("href") != "javascript:;" && link.attr("href") != "") {
			if (link.attr("target") == "_blank") {
				link.attr("href").target = "_blank";
			}
			
			document.location.href = link.attr("href");
			return false;
		}
		
		
		
	});
	
	init();
	

})

var _init_calendarSt;
var _init_calendarEd;

function init(){
				
	var data = {};
	data.govPersonPh='${member.memberPh}';
	
	$.ajax.PromisePost('/gov/viewData/selectGovProjectAndTeam?${_csrf.parameterName}=${_csrf.token}',data)
	.then(showTree);
	_init_calendarSt = new Date();
	_init_calendarSt.setDate(_init_calendarSt.getDate() - 30);
	_init_calendarSt  = getDateFormat(_init_calendarSt);
	_init_calendarEd = getCurrentDateFormat();
	$("#calendarSt").val(_init_calendarSt);
	$("#calendarEd").val(_init_calendarEd);
		
}

function showTree(result,status){
	return new Promise(function(resolve,reject){
		
		console.log('data',result);
		
		var finalHtml = '';
		var current ='';
		var node ='';
		var flag1 = false; //첫번째 인지 아닌지.
		var flag2 = false;
		var flag3 = false;
		var flag4 = false;

		
		result.map(function(item,index){
			console.log('item:',item);
			var html = '';
			if(current == item.memberProject){
				flag1 = true;
				html+=`
					<li data-jstree='{"opened":true, "selected":false }'>`+item.memberTeam+`</li>
	        	`;
			}else{
				if(flag1 == false){
					html = '<ul>';	
					current = item.memberProject;
					html+=`<li data-jstree='{"opened":true}' >`;
					html+=item.memberProject;
					html+=`
							<ul><li data-jstree='{"opened":true, "selected":false }'>`+item.memberTeam+`</li>
			        	`;
			        //line바꾸기
					flag1 = true;
					
				}else{
					html = '</ul></li>';	
					current = item.memberProject;
					html+=`<li data-jstree='{"opened":true}' >`;
					html+=item.memberProject;
					html+=`
							<ul><li data-jstree='{"opened":true, "selected":false }'>`+item.memberTeam+`</li>
			        	`;
			        //line바꾸기
					flag1 = true;					
				}
			}
			
			/* <ul>
			    <li data-jstree='{"opened":true}' >
			      Root node 1
			      <ul>
			        <li data-jstree='{"opened":true, "selected":true }'>Initially Selected</li>
			        <li>Folder 1</li>
					<li>Folder 1-1</li>
					<li>Folder 1-1</li>
			      </ul>
			    </li>
			    <li data-jstree='{"opened":false}' >
			      Root node2
			      <ul>
			        <li data-jstree='{"opened":false, "selected":false }'>Initially Selected</li>
			        <li>Folder 1</li>
					<li>Folder 1-1</li>
					<li>Folder 1-1</li>
			      </ul>
			    </li>
			  </ul>
			  
			  */
			finalHtml +=html;
		});
		 //html += '</ul>';
		 finalHtml +='</ul>';
		
		$("#jstree-default2").append(finalHtml);
		
		$("#jstree-default2").jstree({
			"plugins": ["types"],
			"core": {
				"themes": {
					"responsive": false
				}            
			},
			"types": {
				"default": {
					"icon": "fa fa-folder text-warning fa-lg"
				},
				"file": {
					"icon": "fa fa-file text-inverse fa-lg"
				}
			}
		});
		
/* 		`<ul>
	    <li data-jstree='{"opened":true}' >
	      Root node 1
	      <ul>
	        <li data-jstree='{"opened":true, "selected":true }'>Initially Selected</li>
	        <li>Folder 1</li>
			<li>Folder 1-1</li>
			<li>Folder 1-1</li>
	      </ul>
	    </li>
	    <li data-jstree='{"opened":false}' >
	      Root node2
	      <ul>
	        <li data-jstree='{"opened":false, "selected":false }'>Initially Selected</li>
	        <li>Folder 1</li>
			<li>Folder 1-1</li>
			<li>Folder 1-1</li>
	      </ul>
	    </li>
	  </ul>` */
		
		
	});
}

function click_confirm(param){
	 console.log(param);
	var info = _table1.row(param).data();
	 
	var data= {};
	data.seq= info.seq;
	
	
	
	$.ajax({
   	url: '/gov/viewData/confirm?${_csrf.parameterName}=${_csrf.token}',
       data:data,
       type: 'post',
       success:function(data){ //"inedx3"
        	
      	 console.log('success');
      	 console.dir(data);
      	alert("확정 되었습니다.");
      	var data = _clickTreeValues;
      	makeTeamClickDt(data);
       
       },	//success끝 
	     error : function(error) {
	    	 console.log('error');
	    	 console.dir(error);
	    	 alert("관리자에게 문의해주세요.");
	     },
	     complete : function() {
	        // alert("complete!");    
	     }
   });	
	
	
} 



function editColumn(param){
	
	
	if(_editing == false){
		_editing = true;
		_editingRow = param;
		$.extend(true,original_target,_table1.row(param).data());    
	    
	    
	    var change_target = _table1.row(param).cell(param,6);
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
		
	    var change_target = _table1.row(param).cell(param,7);
	    var value = change_target.data();
	    var html ="";
	    html += '<select name="attendYn" id="attendYn">';
	    html += '<option value="출석">출석</option>';
    	html += '<option value="">결석</option>';
	    html += '</select>';
		change_target.data(html);
		$("#attendYn").val(value).prop("selected", true);
		
		
	    var change_target = _table1.row(param).cell(param,12);
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
			
		    var change_target = _table1.row(param).cell(param,6);
		    change_target.data($("#memberNm").val());
		    edit_target.memberNm = change_target.data();
		    
		    var change_target = _table1.row(param).cell(param,7);
		    change_target.data($("#attendYn").val());
		    edit_target.attendYn = change_target.data();
		    
		    var change_target = _table1.row(param).cell(param,12);
		    change_target.data($("#matchingNm").val());
		    edit_target.matchingNm = change_target.data();
		    
		    
		    var change_target = _table1.row(param).cell(param,13);
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
			
			
			EDIT_LOG(original_target,edit_target);
		}
		
	}
	

	function EDIT_LOG(origin,edit){
		
		origin = JSON.stringify(origin);
		edit = JSON.stringify(edit);
		
	    $.ajax({
	    	url: '/gov/viewData/edit_new?${_csrf.parameterName}=${_csrf.token}',
	        data:{origin:origin,edit:edit},
	        type: 'post',
	        success:function(data){ //"inedx3"
	         	
	        	if(data.result){
	        		alert('수정완료.')
	        		return true;
	        	}else{
	        		alert('수정실패. 관리자에게 문의하세요.')
	        		return false;
	        	}
	        
	        },	//success끝 
		     error : function(error) {
		    	 console.log('error');
		    	 console.dir(error);
		    	 alert("수정사항의 형식을 확인해주세요(ex:교육날짜:YYYY-MM-DD, 시작 및 종료시간 HH:mm)");
		     },
		     complete : function() {
		        // alert("complete!");    
		     }
	    });	
		
	}

	

    
}
$('#searchBtn').on('click', function () {
	 
	 var target = $("#searchColum option:selected").val();
	 var targetText = $("#searchColumText").val();
	 if(target == 'startHhmm'){
		 _table1
        .columns([2])
        .search(targetText)
        .draw(); 
	 }else if(target == 'endHhmm'){
		 _table1
        .columns([3])
        .search(targetText)
        .draw(); 
	 }else if(target == 'memberProject'){
		 _table1
        .columns([4])
        .search(targetText)
        .draw(); 
		 
	 }else if(target == 'eduDt'){
		 _table1
	        .columns([1])
	        .search(targetText)
	        .draw(); 
			 
		 }
	 
});

function callDataClickTeam(data){
	
	return 	 $.ajax({
    	url: '/gov/viewData/selectGovTREE?${_csrf.parameterName}=${_csrf.token}',
    	data: data,
        type: 'post',
        success:function(data){ 
        
	        studentNames = [];
	        mentorNames = [];
	        $.each(data.data,function(index,item){
	        	studentNames.push(item.matchingNm);
	        	mentorNames.push(item.memberNm);
	        })
	        //con
        	
        //조회시 수정할 데이터 초기화
        commitData = [];
        	
        _table1=$('#datatable').DataTable({
        	dom: 'Brtip',
        	/* dom: 'f<"html5buttons"B><"bottom"i>', */
	    	paging : false,
	    	data:data.data,
	    	"dataSrc": "",
	    	"pagingType" : "full_numbers",
	    	"processing" : true,
	    	"destroy" : true,
	        
	    	"buttons": [
                'copy', 'excel', 'csv', 'print'
            ],
	    	"columns" : [ 
	        	
	        	
	    		{ data: 'seq',render:function(data, type, full, meta){
	        		return '<button class="btn btn-info btn-lg" value="'+data+'" id="btn_'+data+'" onclick="editColumn('+meta.row+')" />수정하기</button>';
				}},
	        	
				{ data: 'yyyyMMdd'},
			    { data: 'startHhmm'},
			    { data: 'endHhmm'},
			    { data: 'memberProject'},
			    { data: 'memberTeam'},
			    { data: 'memberNm'},
			    { data: 'attendYn'},
			    { data: 'memberEmail'},
			    { data: 'govNm'},
			    { data: 'govPersonNm'},
			    { data: 'linkUrl'},
			    { data: 'matchingNm'},
			    { data: 'stAttendYn'},
					{ data: 'confirm', render:function(data, type, full, meta){
	        		
	        		if (data){
	        			return "확정";
	        		}else{
	        			return "미확정";
	        		}
	        	}},
	        	{ data: 'confirm',render:function(data, type, full, meta){
	        		console.log(meta.row);
			    	return '<button class="btn btn-danger btn-lg" value="'+data+'" id="btn_'+data+'" onclick="click_confirm('+meta.row+')" />확정하기</button>';
				}}
			   
			    
	       	],

	    });
        },	//success끝 
	     error : function(error) {
	    	 console.log('error');
	    	 console.dir(error);
	    	 alert("오류가 발생했습니다. 관리자에게 문의하세요.");
	     },
	     complete : function() {
	        // alert("complete!");    
	     }
    });	
}

function makeTeamClickDt(data){
	
	$.ajax({
	    	url: '/gov/viewData/selectGovTREE?${_csrf.parameterName}=${_csrf.token}',
	    	data: data,
	        type: 'post',
	        success:function(data){ 
	        
	        _table1=$('#datatable').DataTable({
	        	dom: 'Brtip',
	        	/* dom: 'f<"html5buttons"B><"bottom"i>', */
		    	paging : false,
		    	data:data.data,
		    	"dataSrc": "",
		    	"pagingType" : "full_numbers",
		    	"processing" : true,
		    	"destroy" : true,
		    	"buttons": [
	                'copy', 'excel', 'csv', 'print'
	            ],
		    	"columns" : [ 
		    	

		    		{ data: 'seq',render:function(data, type, full, meta){
		        		return '<button class="btn btn-info btn-lg" value="'+data+'" id="btn_'+data+'" onclick="editColumn('+meta.row+')" />수정하기</button>';
					}},
		        	
					{ data: 'yyyyMMdd'},
				    { data: 'startHhmm'},
				    { data: 'endHhmm'},
				    { data: 'memberProject'},
				    { data: 'memberTeam'},
				    { data: 'memberNm'},
				    { data: 'attendYn'},
				    { data: 'memberEmail'},
				    { data: 'govNm'},
				    { data: 'govPersonNm'},
				    { data: 'linkUrl'},
				    { data: 'matchingNm'},
				    { data: 'stAttendYn'},
						{ data: 'confirm', render:function(data, type, full, meta){
		        		
		        		if (data){
		        			return "확정";
		        		}else{
		        			return "미확정";
		        		}
		        	}},
		        	{ data: 'confirm',render:function(data, type, full, meta){
		        		console.log(meta.row);
				    	return '<button class="btn btn-danger btn-lg" value="'+data+'" id="btn_'+data+'" onclick="click_confirm('+meta.row+')" />확정하기</button>';
					}}
				    
		       	],

		    });
	        },	//success끝 
 		     error : function(error) {
 		    	 console.log('error');
 		    	 console.dir(error);
 		    	 alert("오류가 발생했습니다. 관리자에게 문의하세요.");
 		     },
 		     complete : function() {
 		        // alert("complete!");    
 		     }
 	    });	
  
 	 console.log('success');
 	 //console.dir(data);
}

</script>



