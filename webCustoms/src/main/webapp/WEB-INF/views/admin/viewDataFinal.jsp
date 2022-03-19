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
			<h1 class="page-header">매칭현황 <!-- <small>header small text goes here...</small> --></h1>
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
	                                <!-- inline -->
									<div class="form-check form-check-inline">
										<input class="form-check-input" type="radio" id="inlineRadio1" name="inlineRadio" checked="checked" value="Y"><label for="inlineRadio1">기간 검색 적용</label> 
									</div>
									<div class="form-check form-check-inline">
										<input class="form-check-input" type="radio" id="inlineRadio2" name="inlineRadio" value="N"><label for="inlineRadio2">기간 검색 미적용</label> 
									</div>
	                                <!-- <input type="file" class="filestyle" data-buttonname="btn-secondary"> -->
	                            </div>
 								<input type="date" id="calendarSt" onchange="calendarSt(event);" style="height: 32px" >
 								<input type="date" id="calendarEd" onchange="calendarEd(event);" style="height: 32px">
 								<input type="text" name="searchColumText" id="searchColumText" placeholder="검색어를 입력해주세요.">
								<select name="searchColum" id="searchColum">
									<option value="eduDt">교육일자</option>
								    <option value="startHhmm">시작시간</option>
								    <option value="endHhmm">종료시간</option>
								    <option value="memberProject">프로젝트명</option>
								    
								</select>
 								<button id="searchBtn" class="btn btn-primary">검색</button>
 								
 								<p id="allData"></p>
	                                        <div style="margin-bottom: 10px">
	                                        <!-- table start -->
		                                        <table id="datatable"    class="table table-bordered nowrap" style="border-collapse: collapse; border-spacing: 0; width: 100%;">
		                                            <thead>
		                                            <tr>
		                                               <!-- <th>더보기</th> -->
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
var _editing = false;
var _editingRow;
var _clickTreeValues;
var _table1; 

var mentorNames = [];
var studentNames = [];
var projectNames = [];
var teamNames = [];

// 기간 검색 미적용 클릭 시
$("input:radio[name='inlineRadio']").change(function() {
    if ($(this).val() === 'N') {
    	var data = _clickTreeValues;
    	if (typeof data=="undefined"){
  		  alert("프로젝트명이나 팀명을 먼저 선택해주세요");
  	  }else{
    	data.searchSt = $("#calendarSt").val();
    	data.searchEd = $("#calendarEd").val();
    	
    	$("input:radio[name='inlineRadio']").map( (index,item) =>{
    		if(item.checked){
    			data.searchStEdYn = item.value;
    		}
    	});
    	
    	 if(Object.keys(_clickTreeValues).length==4){
 	   		makeProjectClickDt(data);
 	   	}else{
 	   		makeTeamClickDt(data);
 	   	}
  	  }
    }
    
  });
//기간 검색 허용버튼 클릭 시
$("input:radio[name='inlineRadio']").change(function() {
    if ($(this).val() === 'Y') {
    	var data = _clickTreeValues;
    	if (typeof data=="undefined"){
  		  alert("프로젝트명이나 팀명을 먼저 선택해주세요");
  	  }else{
    	data.searchSt = $("#calendarSt").val();
    	data.searchEd = $("#calendarEd").val();
    	
    	$("input:radio[name='inlineRadio']").map( (index,item) =>{
    		if(item.checked){
    			data.searchStEdYn = item.value;
    		}
    	});
    	
    	 if(Object.keys(_clickTreeValues).length==4){
 	   		makeProjectClickDt(data);
 	   	}else{
 	   		makeTeamClickDt(data);
 	   	}
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
	   	 if(Object.keys(_clickTreeValues).length==4){
	   		makeProjectClickDt(data);
	   	}else{
	   		makeTeamClickDt(data);
	   	}
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
	     	
	     	 if(Object.keys(_clickTreeValues).length==4){
	     		makeProjectClickDt(data);
	     	}else{
	     		makeTeamClickDt(data);
	     	}
		}
  }
	
		
	



function deleteSeq(){
	
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
	//console.log('deleteSeq',checkedSeq);
var obj ={};
obj.deleteSeq = checkedSeq;
//var dd = checkedSeq;
//console.dir(dd);
	
    $.ajax({
    	url: '/admin/viewData/delete?${_csrf.parameterName}=${_csrf.token}',
        data:obj,
        type: 'post',
        traditional : true,
        success:function(data){ //"inedx3"
        	if(data.result){
        		alert("성공적으로 삭제되었습니다.");
        	}else{
        		alert("성공적으로 삭제되었습니다. xxxx" );
        	}
        	
        	var data = _clickTreeValues;
         	
         	if(Object.keys(_clickTreeValues).length==4){
         		makeProjectClickDt(data);
         	}else{
         		makeTeamClickDt(data);
         	}
        
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

function allCheck(){
	$("input:checkbox[name='seqCheck']").map(function(index,item){
			
		if($("input:checkbox[name='allcheck']")[0].checked == true){
			item.checked = true;
		}else{
			item.checked = false;
		}
	})
}




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
	
	
	if(click[0].parent=='#'){
		var projectName=click[0].text;
		projectName = projectName.replaceAll("\n\t\t\t\t\t\t\t","")
		data.memberProject=projectName;
		_clickTreeValues = data;
		//프로젝트 선택
		callDataClickProject(data);
	
	}else if(click[0].parent!='#'){
		var parentId="#"+click[0].parent+"_anchor";
		var parentValue=$(parentId)[0].innerText;
		var childValue=click[0].text;
		
		
		data.memberProject=parentValue;
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



  // #column3_search is a <input type="text"> element
  $('#searchBtn').on('click', function () {
 	 
 	 var target = $("#searchColum option:selected").val();
 	 var targetText = $("#searchColumText").val();
 	 if(target == 'startHhmm'){
 		 _table1
          .columns([1])
          .search(targetText)
          .draw(); 
 	 }else if(target == 'endHhmm'){
 		 _table1
          .columns([2])
          .search(targetText)
          .draw(); 
 	 }else if(target == 'memberProject'){
 		 _table1
          .columns([3])
          .search(targetText)
          .draw(); 
 		 
 	 }else if(target == 'eduDt'){
		 _table1
	        .columns([0])
	        .search(targetText)
	        .draw(); 
			 
		 }
 	 
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
		//console.log('selected',data);
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
				
	
	var formData = new FormData();
	
	$.ajax.PromisePostForm('/admin/viewData/selectAllProjectAndTeamFinal?${_csrf.parameterName}=${_csrf.token}',formData)
	.then(showTree);
		
	
	_init_calendarSt = new Date();
	_init_calendarSt.setDate(_init_calendarSt.getDate() - 30);
	_init_calendarSt  = getDateFormat(_init_calendarSt);
	_init_calendarEd = getCurrentDateFormat();
	$("#calendarSt").val(_init_calendarSt);
	$("#calendarEd").val(_init_calendarEd);
	//console.log('_init_'+_init_calendarEd)
	//console.log('_init_'+_init_calendarSt)
	
}

function showTree(result,status){
	return new Promise(function(resolve,reject){
		
		
		var finalHtml = '';
		var current ='';
		var node ='';
		var flag1 = false; //첫번째 인지 아닌지.
		var flag2 = false;
		var flag3 = false;
		var flag4 = false;
		projectNames = [];
		teamNames = [];
		result.map(function(item,index){
			var html = '';
			if(current == item.memberProject){
				projectNames.push(item.memberProject);
				flag1 = true;
				html+=`
					<li data-jstree='{"opened":true, "selected":false }'>`+item.memberTeam+`</li>
	        	`;
			}else{
				projectNames.push(item.memberProject);
				if(flag1 == false){
		        	
					teamNames.push(item.memberTeam);
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
					teamNames.push(item.memberTeam);
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
			finalHtml +=html;
		});
		
		teamNames = teamNames.filter((element, index) => {
		    return teamNames.indexOf(element) === index;
		});
		
		projectNames = projectNames.filter((element, index) => {
		    return projectNames.indexOf(element) === index;
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
		

		
		
	});
}



function callDataClickTeam(data){
	
	return 	 $.ajax({
    	url: '/admin/viewData/selectTREEFinal?${_csrf.parameterName}=${_csrf.token}',
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
	        	
	        	{ data: 'yyyyMMdd'},
			    { data: 'startHhmm'},
			    { data: 'endHhmm'},
			    { data: 'memberProject'},
			    { data: 'memberTeam'},
			    { data: 'memberNm'},
			    { data: 'memberEmail'},
			    { data: 'memberPh'},
			    { data: 'memberLeaderYn'},
			    { data: 'govNm'},
			    { data: 'matchingNm'},
			    { data: 'attendYn'},
			    { data: 'govPersonNm'},
			    { data: 'govPersonEmail'},
			    { data: 'govPersonPh'},
			    { data: 'linkUrl'},
			    
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


function callDataClickProject(data){
	
	return 			 $.ajax({
    	url: '/admin/viewData/selectProjectTREEFinal?${_csrf.parameterName}=${_csrf.token}',
    	data: data,
        type: 'post',
        success:function(data){ //"inedx3"
        
           	//수정할 이름 저장.
            studentNames = [];
            mentorNames = [];
	        $.each(data.data,function(index,item){
	        	studentNames.push(item.matchingNm);
	        	mentorNames.push(item.memberNm);
        	});
	        
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
		        	
		        	
		        	
		        	{ data: 'yyyyMMdd'},
				    { data: 'startHhmm'},
				    { data: 'endHhmm'},
				    { data: 'memberProject'},
				    { data: 'memberTeam'},
				    { data: 'memberNm'},
				    { data: 'memberEmail'},
				    { data: 'memberPh'},
				    { data: 'memberLeaderYn'},
				    { data: 'govNm'},
				    { data: 'matchingNm'},
				    { data: 'attendYn'},
				    { data: 'govPersonNm'},
				    { data: 'govPersonEmail'},
				    { data: 'govPersonPh'},
				    { data: 'linkUrl'},
				    
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
	    	url: '/admin/viewData/selectTREEFinal?${_csrf.parameterName}=${_csrf.token}',
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
		    	

		        	{ data: 'yyyyMMdd'},
				    { data: 'startHhmm'},
				    { data: 'endHhmm'},
				    { data: 'memberProject'},
				    { data: 'memberTeam'},
				    { data: 'memberNm'},
				    { data: 'memberEmail'},
				    { data: 'memberPh'},
				    { data: 'memberLeaderYn'},
				    { data: 'govNm'},
				    { data: 'matchingNm'},
				    { data: 'attendYn'},
				    { data: 'govPersonNm'},
				    { data: 'govPersonEmail'},
				    { data: 'govPersonPh'},
				    { data: 'linkUrl'},
				    
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

function makeProjectClickDt(data){
	
	$.ajax({
		url: '/admin/viewData/selectProjectTREEFinal?${_csrf.parameterName}=${_csrf.token}',
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
		    	
		    		
		        	{ data: 'yyyyMMdd'},
				    { data: 'startHhmm'},
				    { data: 'endHhmm'},
				    { data: 'memberProject'},
				    { data: 'memberTeam'},
				    { data: 'memberNm'},
				    { data: 'memberEmail'},
				    { data: 'memberPh'},
				    { data: 'memberLeaderYn'},
				    { data: 'govNm'},
				    { data: 'matchingNm'},
				    { data: 'attendYn'},
				    { data: 'govPersonNm'},
				    { data: 'govPersonEmail'},
				    { data: 'govPersonPh'},
				    { data: 'linkUrl'},
					
				    
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
 	// console.dir(data);
}
</script>



