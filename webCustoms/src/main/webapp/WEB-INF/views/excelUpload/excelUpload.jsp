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
			<h1 class="page-header">BASE INFO 등록 <!-- <small>header small text goes here...</small> --></h1>
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
								<span class="btn btn-primary fileinput-button me-1">
									<i class="fa fa-fw fa-plus"></i>
									<span>파일선택</span>
									<input type="text" onchange="changedata()">
									<input id="file" type="file" name="file" >
								</span>
	                                                <!-- <input type="file" name="file" id="file" accept="application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"> -->
	                                                <!-- <input id="file" type="file" name="file" > -->
	                                                <button type="button" class="btn btn-default" id="clickupload" onclick="upload()">엑셀 데이터 읽기</button>
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
	                                        <div style="margin-bottom: 10px">
	                                        <!-- table start -->
		                                        <table id="datatable"    class="table table-bordered dt-responsive nowrap" style="border-collapse: collapse; border-spacing: 0; width: 100%;">
		                                            <thead>
		                                            <tr>
		                                            	<th>순서</th>
		                                            	<th>업로드가능 여부</th>
		                                                <th>교육날짜</th>
		                                                <th>시작날짜</th>
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
                        				<!-- html -->
<!-- <div id="jstree-default">
  <ul>
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
</div> -->
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
</script>


<script type="text/javascript">

var targetFile;
var fullData = [];



$("#commit").on("click",function(event){
	var table = $('#datatable').DataTable();

	var list =[];
	console.dir(fullData);
	
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


function changedata(data,meta){
	
	console.dir(data);
	console.dir(meta);
	
}

function upload(){
				
	targetFile = $("#file")[0].files[0];
	var formData = new FormData();
	var file = document.getElementById("file");
	formData.append("file", file.files[0]);

	fullData = [];
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
		
	   var table1 =  $('#datatable').DataTable({
	    	dom: '<"top"f><"html5buttons"B><"bottom"i>',
	    	paging : false,
	    	data:result.data,
	    	searching: true,
	    	"dataSrc": "",
	    	"pagingType" : "full_numbers",
	    	"processing" : true,
	    	"destroy" : true,
	        "columns" : [ 
	        	
	        	{ data: 'lineNumber'},
	        	{ data: 'uploadAvailable' ,render:function(data, type, full, meta){
	        		if(data == 'Y'){
	        			return '<div  style="color: green" >업로드 가능</div>';	
	        		}else{
	        			return '<div  style="color: red" >업로드 불가</div>';
	        		}
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

	       ],
	       language : LANG_KOR,
	       "drawCallback": function( settings ) {
	    		var table = $('#datatable').DataTable();
	    		table.rows().every( function () {
	    		    var d = this.data();
	    		    fullData.push(d);
	    		})
	           //alert( 'DataTables has redrawn the table' );
	       },
	       
	    });
	    	datatableEdit({
	    	dataTable : table1,
	    	columnDefs : [
	    	{
	    		targets : 2
	    	}
	      ]
	   	});


	});
}




</script>

