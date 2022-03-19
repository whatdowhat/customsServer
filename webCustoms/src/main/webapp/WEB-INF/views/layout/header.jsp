<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"  isELIgnored="false"%>
<!-- BEGIN navbar-header -->
<div class="navbar-header">
	<a href="#" class="navbar-brand"><span class="navbar-logo"></span>
		<b>Mentor</b> App</a>
	<button type="button" class="navbar-mobile-toggler"
		data-toggle="app-sidebar-mobile">
		<span class="icon-bar"></span> <span class="icon-bar"></span> <span
			class="icon-bar"></span>
	</button>
</div>

<!-- END navbar-header -->
<!-- BEGIN header-nav -->
<div class="navbar-nav">

	<div class="navbar-item navbar-user dropdown">
		<a href="#"
			class="navbar-link dropdown-toggle d-flex align-items-center"
			data-bs-toggle="dropdown"> 
		<span> 
		<span class="d-none d-md-inline" style="font-size:18px; ">Login : ${member.memberId} </span>
		</span>
		</a>
		<div class="dropdown-menu dropdown-menu-end me-1">
			<a href="/logout" class="dropdown-item">Log Out</a>
		</div>
	</div>
</div>
<!-- END header-nav -->
