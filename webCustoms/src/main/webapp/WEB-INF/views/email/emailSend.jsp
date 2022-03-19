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
						
									<input type="text" id="address"  placeholder="받는 이메일 주소">
									<input type="text" id="title"  placeholder="메일 제목"> 
									<input type="text" id="message"  placeholder="메일 내용">
									<button id="emailBtn">보내기</button>
									
							<div class="table-responsive">
 								
	                                        <div style="margin-bottom: 10px">
	                                        <!-- table start -->
		                                        <table id="datatable"    class="table table-bordered nowrap" style="border-collapse: collapse; border-spacing: 0; width: 100%;">
		                                            <thead>
		                                            <tr>
		                                               <!-- <th>더보기</th> -->
		                                               <th width="20px"><input type="checkbox" name="allcheck" onchange="allCheck()" > 선택 </th>
		                                               	<th>수정</th>
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

<script type="text/javascript">

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
	   	 console.dir(data);
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


</script>

