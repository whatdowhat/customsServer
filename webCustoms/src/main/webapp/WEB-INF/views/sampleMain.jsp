<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

		<div id="content" class="app-content">
			<!-- BEGIN breadcrumb -->
			<ol class="breadcrumb float-xl-end">
				<li class="breadcrumb-item"><a href="javascript:;">Home</a></li>
				<li class="breadcrumb-item"><a href="javascript:;">Tables</a></li>
				<li class="breadcrumb-item active">Basic Tables</li>
			</ol>
			<!-- END breadcrumb -->
			<!-- BEGIN page-header -->
			<h1 class="page-header">Basic Tables <small>header small text goes here...</small></h1>
			<!-- END page-header -->
			<!-- BEGIN row -->
			<div class="row">
				<!-- BEGIN col-6 -->
				<div class="col-xl-6">
					<!-- BEGIN panel -->
					<div class="panel panel-inverse" data-sortable-id="table-basic-1">
						<!-- BEGIN panel-heading -->
						<div class="panel-heading">
							<h4 class="panel-title">Default Table</h4>
							<div class="panel-heading-btn">
								<a href="javascript:;" class="btn btn-xs btn-icon btn-default" data-toggle="panel-expand"><i class="fa fa-expand"></i></a>
								<a href="javascript:;" class="btn btn-xs btn-icon btn-success" data-toggle="panel-reload"><i class="fa fa-redo"></i></a>
								<a href="javascript:;" class="btn btn-xs btn-icon btn-warning" data-toggle="panel-collapse"><i class="fa fa-minus"></i></a>
								<a href="javascript:;" class="btn btn-xs btn-icon btn-danger" data-toggle="panel-remove"><i class="fa fa-times"></i></a>
							</div>
						</div>
						<!-- END panel-heading -->
						<!-- BEGIN panel-body -->
						<div class="panel-body">
	                        <label class="form-label">파일선택</label>
	                        <!-- <input type="file" name="file" id="file" accept="application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"> -->
	                        <input id="file" type="file" name="file" >
	                        <input type="button" value="업로드"  id="clickupload" onclick="upload()">
						</div>
						<!-- END hljs-wrapper -->
					</div>
					<!-- END panel -->
				</div>
				<!-- END col-6 -->
			</div>
			<!-- END row -->
		</div>
    
<script type="text/javascript">

var targetFile;




$("#commit").on("click",function(event){
	var table = $('#datatable').DataTable();

	var validation = true;
	var list =[];
	
	
	table.rows().every( function () {
	    var d = this.data();
	 	list.push(d);
	    console.dir(d);
	    
	});
	
	
    $.ajax({
    	url: '/excelUpload/data02?${_csrf.parameterName}=${_csrf.token}',
        data:list,
        type: 'post',
        success:function(data){ //"inedx3"
         	
       	 console.log('success');
       	 console.dir(data);
        
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

	
	
	
	
	
});


 

$('#file').change(function(e) {
	
	targetFile = e.target.files[0];
});

function upload(){
	
				var formData = new FormData();
			    var file = document.getElementById("file");
					formData.append("file", file.files[0]);
					//formData.append("fromJSP", fromcontroller.TOCOMP+":"+fromcontroller.ITEMWRTNO);
					
					$.ajax({
						url: '/test/data01?${_csrf.parameterName}=${_csrf.token}',
						data: formData,
						processData: false,
						contentType: false,
						type: 'post',
						success: function(data){

						    console.dir(data);
						
						},
						error: function(error) { 
							console.log('error');
			    	    	 console.dir(error);
							alert("오류가 발생했습니다. 관리자에게 문의하세요.");
			            }       
					});// ajax
					
		
}

</script>
    