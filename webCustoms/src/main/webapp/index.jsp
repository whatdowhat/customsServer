<%@ page language="java" contentType="text/html; charset=EUC-KR"
    pageEncoding="EUC-KR"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 
<!DOCTYPE html>
<html lang="en">
<head>
	<meta charset="utf-8" />
	<title>Mento | Login</title>
	<meta content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no" name="viewport" />
	<meta content="" name="description" />
	<meta content="" name="author" />
	
	<!-- ================== BEGIN core-css ================== -->
	<link href="https://fonts.googleapis.com/css?family=Open+Sans:300,400,600,700" rel="stylesheet" />
	<link href="../assets/css/vendor.min.css" rel="stylesheet" />
	<link href="../assets/css/default/app.min.css" rel="stylesheet" />
	<!-- ================== END core-css ================== -->
</head>
<body class='pace-top'>
	<!-- BEGIN #loader -->
	<div id="loader" class="app-loader">
		<span class="spinner"></span>
	</div>
	<!-- END #loader -->

	<!-- BEGIN #app -->
	<div id="app" class="app">
		<!-- BEGIN login -->
		<div class="login login-with-news-feed">
			<!-- BEGIN news-feed -->
			<div class="news-feed">
				<div class="news-image" style="background-image: url(../assets/img/login-bg/login-bg-11.jpg)"></div>
				<div class="news-caption">
					<h4 class="caption-title"><b>Mentor</b> App </h4>
					<p>
						<!-- Download the Color Admin app for iPhone®, iPad®, and Android™. Lorem ipsum dolor sit amet, consectetur adipiscing elit. -->
					</p>
				</div>
			</div>
			<!-- END news-feed -->
			
			<!-- BEGIN login-container -->
			<div class="login-container">
				<!-- BEGIN login-header -->
				<div class="login-header mb-30px">
					<div class="brand">
						<div class="d-flex align-items-center">
							<span class="logo"></span>
							<b>Mentor</b> App
						</div>
						<!-- <small>Bootstrap 5 Responsive Admin Template</small> -->
					</div>
					<div class="icon">
						<i class="fa fa-sign-in-alt"></i>
					</div>
				</div>
				<!-- END login-header -->
				
				<!-- BEGIN login-content -->
				<div class="login-content">
					<form action="/login" method="POST" class="fs-13px">
						<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
						<div class="form-floating mb-15px">
							<input type="text" class="form-control h-45px fs-13px" placeholder="핸드폰 번호" id="id" name="id" />
							<label for="emailAddress" class="d-flex align-items-center fs-13px text-gray-600">핸드폰 번호</label>
						</div>
						<div class="form-floating mb-15px">
							<input type="password" class="form-control h-45px fs-13px" placeholder="Password" id="password" name="password" />
							<label for="password" class="d-flex align-items-center fs-13px text-gray-600">패스워드</label>
						</div>
						<div class="form-check mb-30px">
							<c:if test="${errorMessage!=null }">
								 	<span style="color: red">${errorMessage }</span>
							</c:if>
						</div>
						<!-- <div class="form-check mb-30px">
							<input class="form-check-input" type="checkbox" value="1" id="rememberMe" />
							<label class="form-check-label" for="rememberMe">
								아이디 저장
							</label>
						</div> -->
						<div class="mb-15px">
							<button type="submit" class="btn btn-success d-block h-45px w-100 btn-lg fs-14px">로그인</button>
						</div>
<!-- 						<div class="mb-40px pb-40px text-inverse">
							Not a member yet? Click <a href="register_v3.html" class="text-primary">여기</a> 등록
						</div> -->
						<hr class="bg-gray-600 opacity-2" />
						<div class="text-gray-600 text-center text-gray-500-darker mb-0">
							&copy; Mentor App All Right Reserved 2022
						</div>
					</form>
				</div>
				<!-- END login-content -->
			</div>
			<!-- END login-container -->
		</div>
		<!-- END login -->
		
		<!-- BEGIN scroll-top-btn -->
		<a href="javascript:;" class="btn btn-icon btn-circle btn-success btn-scroll-to-top" data-toggle="scroll-to-top"><i class="fa fa-angle-up"></i></a>
		<!-- END scroll-top-btn -->
	</div>
	<!-- END #app -->
	
	<!-- ================== BEGIN core-js ================== -->
	<script src="../assets/js/vendor.min.js"></script>
	<script src="../assets/js/app.min.js"></script>
	<script src="../assets/js/theme/default.min.js"></script>
	<!-- ================== END core-js ================== -->
</body>
</html>