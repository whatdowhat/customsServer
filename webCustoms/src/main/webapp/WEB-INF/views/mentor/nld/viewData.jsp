<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 
<%@ include file="/WEB-INF/views/layout/resources.jsp" %>
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
  	<%@ include file="/WEB-INF/views/layout/header.jsp" %>

  </div>
  <!-- BEGIN #header -->
  <!-- BEGIN #sidebar -->
	<%@ include file="/WEB-INF/views/layout/nav.jsp" %>
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
	                                                <button type="button" class="btn btn-default" id="clickupload" onclick="deleteSeq()">데이터 삭제</button>
	                                                
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
 								<input type="date" id="calendarSt" style="height: 32px" >
 								<input type="date" id="calendarEd" style="height: 32px">
 								<input type="text" name="searchColumText" id="searchColumText" placeholder="검색어를 입력해주세요.">
								<select name="searchColum" id="searchColum">
									<option value="eduDt">교육일자</option>
								    <option value="startHhmm">시작시간</option>
								    <option value="endHhmm">종료시간</option>
								    <option value="memberProject">프로젝트명</option>
								    
								</select>
 								<button id="searchBtn" class="btn btn-primary">검색</button>
 								<button id="showAll" class="btn btn-primary" style="display: none;">전체 데이터 보기</button>
 								<p id="allData"></p>
	                                        <div style="margin-bottom: 10px">
	                                        <!-- table start -->
		                                        <table id="datatable"    class="table table-bordered nowrap" style="border-collapse: collapse; border-spacing: 0; width: 100%;">
		                                            <thead>
		                                            <tr>
		                                               <!-- <th>더보기</th>
		                                               <th><input type="checkbox" name="allcheck" onchange="allCheck()" > select </th> -->
		                                                <th>교육날짜</th>
		                                                <th>시작시간</th>
		                                                <th>종료시간</th>
		                                                <th>프로젝트명</th>
		                                                <th>멘토 팀명</th>
		                                                <th>멘토 이름</th>
		                                                <th>기관명</th>
		                                                <th>매칭된 학생</th>
		                                                <th>출석여부</th>
		                                                <th>기관담당자 이름</th>
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

var _table1; 
var controller;


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
	
}

function allCheck(){
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

	if(click[0].parent!='#'){
	var parentId="#"+click[0].parent+"_anchor";
	var parentValue=$(parentId)[0].innerText;
	var childValue=click[0].text;
	console.log(click);
	console.log(click[0].text);
	console.log(parentId);
	console.log(parentValue);
	
	var data={};
	data.memberProject=parentValue
	data.memberTeam=childValue;
	data.memberPh='${member.memberPh}';
	
	$("input:radio[name='inlineRadio']").map( (index,item) =>{
		if(item.checked){
			data.searchStEdYn = item.value;
		}
	});
	
	data.searchSt = $("#calendarSt").val();
	data.searchEd = $("#calendarEd").val();
	
	 $.ajax({
	    	url: '/nld/viewData/selectNldTREE?${_csrf.parameterName}=${_csrf.token}',
	    	data: data,
	        type: 'post',
	        success:function(data){ //"inedx3"
	        
	        //조회시 수정할 데이터 초기화
	        commitData = [];
	        	
	       	 console.log('success');
	       	 console.dir(data);
	        
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
		        	/* { data: 'yyyyMMdd'}, */ 
		        	
		        	/* { data: 'seq',render:function(data, type, full, meta){
		        		return data;
		        		return '더보기'; 
					}},
		        	{ data: 'seq',render:function(data, type, full, meta){
		        		return `<input type="checkbox" name=seqCheck  value=`+data+` />`; 
					}}, */
		        	
					{ data: 'yyyyMMdd'},
				    { data: 'startHhmm'},
				    { data: 'endHhmm'},
				    { data: 'memberProject'},
				    { data: 'memberTeam'},
				    { data: 'memberNm'},
				    { data: 'govNm'},
				    { data: 'matchingNm'},
				    { data: 'attendYn'},
				    { data: 'govPersonNm'},
				    { data: 'linkUrl'}
				    
		       	],

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
				        .columns([1])
				        .search(targetText)
				        .draw(); 
						 
					 }
				 
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
	data.memberPh='${member.memberPh}';
	$.ajax.PromisePost('/nld/viewData/selectNldProjectAndTeam?${_csrf.parameterName}=${_csrf.token}',data)
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
		
		
		
	});
}



</script>



