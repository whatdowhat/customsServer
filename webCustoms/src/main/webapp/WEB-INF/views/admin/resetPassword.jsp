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
			<h1 class="page-header">사용자 관리 <!-- <small>header small text goes here...</small> --></h1>
			
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
						<div class="table-responsive">
								   
 								<button id="resetPassword" class="btn btn-info btn-lg">비밀번호 초기화</button>
 								<button id="deleteMember" class="btn btn-danger btn-lg">사용자 삭제</button>
 								
	                                        <div style="margin-bottom: 10px">
	                                        <!-- table start -->
		                                        <table id="datatable"    class="table table-bordered nowrap" style="border-collapse: collapse; border-spacing: 0; width: 100%;">
		                                           <thead>
		                                           	<tr>
		                                               <!-- <th>더보기</th> -->
		                                               <th><input type="checkbox" name="allcheck" onchange="allCheck()" > 선택 </th>
		                                               <th>대표 프로젝트</th>
		                                               <th>대표 팀</th>
		                                               <th>비밀번호 초기화여부</th>
		                                               <th>ID</th>
		                                               <th>권한</th>
		                                               <th>멘토이름</th>
		                                               <th>멘토전화번호</th>
		                                               
		                                                
		                                                
		                                            </tr>
		                                           </thead>
		                                        </table>
	                                        </div>
                                    
                        				</div>
                                        
                                       
                          
                        <!-- end row -->
                        
                                      
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

function allCheck(){
	$("input:checkbox[name='seqCheck']").map(function(index,item){
			
		if($("input:checkbox[name='allcheck']")[0].checked == true){
			item.checked = true;
		}else{
			item.checked = false;
		}
	})
}




$("#deleteMember").on("click",function(event){
	
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
	
	
	var result = confirm("사용자 삭제시 복원할 수 없습니다.");
	if(result){
	    
	}else{
	    return false;
	}
	
var obj ={};
obj.resetSeq = checkedSeq;	
   
$.ajax({
    	url: '/admin/delete/member?${_csrf.parameterName}=${_csrf.token}',
    	data:obj,
    	type: 'post',
        traditional : true,
        success:function(data){ //"inedx3"
       	if(data.result){
    		alert("삭제 완료.");
    	}else{
    		alert("삭제 실패. 관리자에게 문의하세요.");
    	}
       	makeDt();
        
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

	
});

$("#resetPassword").on("click",function(event){
	
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
	console.log('resetSeq',checkedSeq);
var obj ={};
obj.resetSeq = checkedSeq;	
   
$.ajax({
    	url: '/admin/reset/password?${_csrf.parameterName}=${_csrf.token}',
    	data:obj,
    	type: 'post',
        traditional : true,
        success:function(data){ //"inedx3"
         	
       	 
       	if(data.result){
    		alert("비밀번호 초기화 완료.");
    	}else{
    		alert("일부 비밀번호 초기화 완료.");
    	}
       	makeDt();
        
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

	
});


function makeDt(){
	
	$.ajax({
    	url: '/admin/reset/makeDt?${_csrf.parameterName}=${_csrf.token}',
        type: 'post',
        success:function(data){ //"inedx3"
         	
        	_table1=$('#datatable').DataTable({
	        	dom: 'frtip',
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
		        		return `<input type="checkbox" name=seqCheck  value=`+data+` />`; 
					}}, 
					
		        	{data : 'memberProject'},
		        	{data : 'memberTeam'},
					{data: 'memberFirstLogin', render:function(data, type, full, meta){
		        		
		        		if (data){
		        			return "초기화";
		        		}else{
		        			return "미초기화";
		        		}
		        			}},
					{ data: 'memberId'},
				    { data: 'memberAuth', render:function(data, type, full, meta){
		        		
		        		if (data=="NLD"){
		        			return "멘토";
		        		}else if(data=="LD"){
		        			return "리더";
		        		}else if(data=="GOV"){
		        			return "기관";
		        		}
		        			}},
				    { data: 'memberNm'},
				    { data: 'memberPh'}
				    
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
	
	}

$(function(){
	makeDt();
	
});

</script>

