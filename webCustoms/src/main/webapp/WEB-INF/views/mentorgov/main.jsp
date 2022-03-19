<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 
<%@ include file="../layout/resources.jsp" %>
<body class='pace-top'>
	<!-- BEGIN #loader -->
	<div id="loader" class="app-loader">
		<span class="spinner"></span>
	</div>
	<!-- END #loader -->

	<!-- BEGIN #app -->
	<div id="app" class="app">
		<!-- BEGIN coming-soon -->
		<div class="coming-soon">
			<!-- BEGIN coming-soon-header -->
			<div class="coming-soon-header">
				<div class="bg-cover"></div>
				<div class="brand">
					<span class="logo"></span> <b>Mentor</b> App
				</div>
			</div>
			<!-- END coming-soon-header -->
			<!-- BEGIN coming-soon-content -->
			<div class="coming-soon-content">
				<div class="desc">
					첫 로그인시 <b>패스워드</b>를 변경해야 메인화면으로 이동가능합니다.
				</div>
				<div class="input-group input-group-lg mx-auto mb-2">
					<span class="input-group-text border-0  bg-gray-200"><i class="ion ion-md-checkbox"></i></span>
					<input type="password" class="form-control fs-13px border-0 shadow-none ps-0 bg-gray-200" placeholder="새로운 비밀번호" id="password1"/>
					
				</div>
				<div class="input-group input-group-lg mx-auto mb-2">
					<span class="input-group-text border-0  bg-gray-200"><i class="fa ion-md-checkbox"></i></span>
					<input type="password" class="form-control fs-13px border-0 shadow-none ps-0 bg-gray-200" placeholder="새로운 비밀번호 확인" id="password2"/>
					
				</div>
				<div class="input-group input-group-lg mx-auto mb-2" >
					<button type="button" class="btn fs-13px btn-inverse" style="margin: auto; width:100%" onclick="chagePassword()">변경</button>
				</div>
				
				<script type="text/javascript">
					function chagePassword(){
						if($("#password1").val() != $("#password2").val()){
							alert('비밀번호를 다시 확인해주세요.');
							return false;
						}
						
						var formData = new FormData();
						var file = document.getElementById("file");
						formData.append("password1", $("#password1").val());
						formData.append("password2", $("#password2").val());
						
						$.ajax.PromisePostForm('/member/chagePassword?${_csrf.parameterName}=${_csrf.token}',formData)
						.then(readEexcel);
						
					}
					

					function readEexcel(result,status){
						return new Promise(function(resolve,reject){
							if(result.result){
								
								alert(result.errMessage);
								var formData = new FormData();
								
								window.location.href = '/member/chagePasswordAfter?${_csrf.parameterName}=${_csrf.token}'; 
								//$.ajax.PromisePostForm('/login/chagePasswordAfter?${_csrf.parameterName}=${_csrf.token}',formData)
								
							}else{
								alert(result.errMessage);
								return false;
							}
							
						});
					}
				
				</script>
				
				<p style="color: red">비밀번호는 특수문자,문자,숫자 모두 포함 8자이상으로 설정해주셔야합니다.</p>
				<p style="color: red">비밀번호 분실 시 관리자에게 문의해주세요.</p>
				
			</div>
			<!-- END coming-soon-content -->
		</div>
		<!-- END coming-soon -->
		
		<!-- END theme-panel -->
		<!-- BEGIN scroll-top-btn -->
		<a href="javascript:;" class="btn btn-icon btn-circle btn-success btn-scroll-to-top" data-toggle="scroll-to-top"><i class="fa fa-angle-up"></i></a>
		<!-- END scroll-top-btn -->
	</div>
	<!-- END #app -->
</body>
