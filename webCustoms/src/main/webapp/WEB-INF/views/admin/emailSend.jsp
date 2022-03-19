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
						
									<input type="text" id="address" value="rhengh01@gmail.com">
									<input type="text" id="title" value="제목">
									<input type="text" id="message" value="메세지.">
									<button id="emailBtn">보내기</button>
									
 								<div class="table-responsive" style="margin-top: 50px;">
 								
	                                        <!-- table start -->
		                                        <table id="datatable"    class="table table-bordered nowrap" style="border-collapse: collapse; border-spacing: 0; width: 100%;">
		                                            <thead>
		                                            <tr>
		                                               <!-- <th>더보기</th> -->
		                                               	<th>수신 이메일</th>
		                                                <th>제목</th>
		                                                <th>내용</th>
		                                                <th>보낸날짜</th>
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

var _table1;
init();
$("#emailBtn").on("click",function(){
	
	var obj = {};
	obj.address = $("#address").val();
	obj.title = $("#title").val();
	obj.message = $("#message").val();
	
	$.ajax({
		url: '/email/send?${_csrf.parameterName}=${_csrf.token}',
	    data:obj,
	    type: 'post',
	    success:function(data){ //"inedx3"
	   	 	alert('발송 완료.')
	   		callData();
	    },	//success끝 
	     error : function(error) {
	    	 console.log('error');
	    	 console.dir(error);
	    	 alert("수정사항의 형식을 확인해주세요(ex:교육날짜:YYYY-MM-DD, 시작 및 종료시간 HH:mm)");
	     },
	     complete : function() {
	     }
	});	
});


function init(){
	
	callData();
}


function callData(){
	var data ={};
	
  	 return $.ajax({
	    	url: '/admin/email/send/select?${_csrf.parameterName}=${_csrf.token}',
	    	data: data,
	        type: 'post',
	        success:function(data){ 
	        
	        _table1=$('#datatable').DataTable({
	        	/* dom: 'Brtip', */
	        	dom: 'f<"html5buttons"B><"bottom"i>',
		    	paging : false,
		    	data:data,
		    	"dataSrc": "",
		    	"pagingType" : "full_numbers",
		    	"processing" : true,
		    	"destroy" : true,
		    	"buttons": [
	                'copy', 'excel', 'csv', 'print'
	            ],
		    	"columns" : [ 
		    	
		        	{ data: 'address'},
				    { data: 'title'},
				    { data: 'message'},
				    { data: 'regDt'},
				    
		       	],
		       	order: [[3, 'desc']],

		    });
	        
	        
	        
	        },	//success끝 
		     error : function(error) {
		    	 console.log('error');
		    	 console.dir(error);
		    	 alert("오류가 발생했습니다. 관리자에게 문의하세요.");
		     },
		     complete : function() {
		     }
	    });	
	
}
</script>

