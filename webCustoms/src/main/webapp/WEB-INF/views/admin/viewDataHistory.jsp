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
			<h1 class="page-header">수정이력 <!-- <small>header small text goes here...</small> --></h1>
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

 
 <br>
 <br>
 <br>
 <br>
 <br>
 								<div class="table-responsive">
	                                        <div style="margin-bottom: 10px">
	                                        <!-- table start -->
		                                        <table id="datatable"    class="table table-bordered nowrap" style="border-collapse: collapse; border-spacing: 0; width: 100%;">
		                                            <thead>
		                                            <tr>
		                                               <!-- <th>더보기</th> -->
		                                               
		                                                <th>수정날짜</th>
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
		                                                <th>학생출석여부</th>
		                                                <th>확정</th>
		                                            </tr>
		                                            </thead>
		                                        </table>
	                                        </div>
                                    
                        				</div>
                        				
 								<div class="table-responsive">
	                                        <div style="margin-bottom: 10px">
	                                        <!-- table start -->
		                                        <table id="datatable2"    class="table table-bordered nowrap" style="border-collapse: collapse; border-spacing: 0; width: 100%;">
		                                            <thead>
		                                            <tr>
		                                               <!-- <th>더보기</th> -->
		                                                <th>수정날짜</th>
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
		                                                <th>학생출석여부</th>
		                                                <th>확정</th>
		                                                <th>복원</th>
		                                                
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

$(function(){
	init();
})

var _table1;
var _table2;

function init(){
	
	return 	 $.ajax({
    	url: '/admin/viewDataHistory/selectAll?${_csrf.parameterName}=${_csrf.token}',
    	data: {},
        type: 'post',
        success:function(data){ //"inedx3"
        
        _table1=$('#datatable').DataTable({
        	dom: 'Brtip',
        	/* dom: 'f<"html5buttons"B><"bottom"i>', */
	    	paging : false,
	    	data:data,
	    	order: [[1, 'asc'],[2, 'asc']],
	    	
	    	"dataSrc": "",
	    	"pagingType" : "full_numbers",
	    	"processing" : true,
	    	"destroy" : true,
	        
	    	"buttons": [
                'copy', 'excel', 'csv', 'print'
            ],
            
	    	"columns" : [ 
	        	
	        	{ data: 'editDt',render:function(data, type, full, meta){
        			
	        		data = (data || '-');
	        		return data;
				}},
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
			    { data: 'stAttendYn'},
				{ data: 'confirm'}
			    
			    
	       	],

	    });
        
        
		$('#datatable tbody').on( 'click', 'tr td', function () {
			
			var targetCell = _table1.row(this);
			var seq = targetCell.data().seq;
			
			$.ajax({
		    	url: '/admin/viewDataHistory/findByBaseInfoSeq?${_csrf.parameterName}=${_csrf.token}',
		    	data: {seq:seq},
		        type: 'post',
		        success:function(data){ //"inedx3"
		        
		        _table2=$('#datatable2').DataTable({
		        	dom: 'Brtip',
		        	/* dom: 'f<"html5buttons"B><"bottom"i>', */
			    	paging : false,
			    	data:data,
			    	order: [[0, 'desc']],
			    	ordering: true,
			    	"dataSrc": "",
			    	"pagingType" : "full_numbers",
			    	"processing" : true,
			    	"destroy" : true,
			        
			    	"buttons": [
		                'copy', 'excel', 'csv', 'print'
		            ],
			    	"columns" : [ 
			        	
			        	
			        	{ data: 'editDt',render:function(data, type, full, meta){
		        			
			        		data = (data || '-');
			        		return data;
						}},
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
					    { data: 'stAttendYn'},
	   					{ data: 'confirm'},
					    { data: 'seq',render:function(data, type, full, meta){
			        		console.log(meta.row);
					    	return '<button value="'+data+'" class="btn btn-primary" id="btn_'+data+'" onclick="restore('+meta.row+')" />복원하기</button>';  
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
			        console.log('complete called');
			     }
		    });		        
			
			
	     })
        
        
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
    });	
}
 function restore(param){
	 console.log(param);
	var info = _table2.row(param).data();
	 console.log(info.seq);
	console.log(info);
	 var data= {};
	data.seq= info.baseInfoSeq;
	data.yyyyMMdd = info.yyyyMMdd;
	data.startHhmm = info.startHhmm;
	data.endHhmm = info.endHhmm;
	data.memberProject = info.memberProject;
	data.memberTeam = info.memberTeam;
	data.memberNm = info.memberNm;
	data.memberEmail = info.memberEmail;
	data.memberPh = info.memberPh;
	data.memberLeaderYn=info.memberLeaderYn;
	data.govNm = info.govNm;
	data.matchingNm = info.matchingNm;		
	data.attendYn = info.attendYn;
	data.govPersonNm = info.govPersonNm;
	data.govPersonEmail =info.govPersonEmail;
	data.govPersonPh = info.govPersonPh;
	data.linkUrl = info.linkUrl;
	data.stAttendYn = info.stAttendYn;
	data.confirm = info.confirm;
	
	
	$.ajax({
    	url: '/admin/viewDataHistory/restore?${_csrf.parameterName}=${_csrf.token}',
        data:data,
        type: 'post',
        success:function(data){ //"inedx3"
         	
       	 console.log('success');
       	 console.dir(data);
       	init();
       	alert("데이터가 복원 되었습니다.");
       	
        
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
	
	
	
} 

</script>



