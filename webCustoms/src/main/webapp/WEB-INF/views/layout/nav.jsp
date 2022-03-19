<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"  isELIgnored="false"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<%@page import="com.keepgo.whatdo.entity.Auth"%>

<%-- <%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%> --%>
<%-- <%@ page import="com.keepgo.whatdo.entity.Auth %> --%>

<!DOCTYPE html>
		<!-- BEGIN #sidebar -->
		<div id="sidebar" class="app-sidebar">
			<!-- BEGIN scrollbar -->
			<div class="app-sidebar-content" data-scrollbar="true" data-height="100%">
				<!-- BEGIN menu -->
				<div class="menu">
					<div class="menu-item">
						<sec:authorize access="hasAuthority('SUPER')">
						<a href="/admin/loginAfterSuer" class="menu-link">
							<div class="menu-icon">
								<i class="fa fa-upload"></i>
							</div>
							<div class="menu-text">엑셀업로드</div>
						</a>
						 
						<%-- <sec:authorize access="isAuthenticated()"> --%>
						<a href="/admin/viewData" class="menu-link">
							<div class="menu-icon">
								<i class="fas fa-address-card"></i>
							</div>
							<div class="menu-text">매칭하기</div>
						</a>
						<a href="/admin/viewDataHistory" class="menu-link">
							<div class="menu-icon">
								<i class="fas fa-file-alt"></i>
							</div>
							<div class="menu-text">수정내역</div>
						</a>
						<a href="/admin/resetPassword" class="menu-link">
							<div class="menu-icon">
								<i class="fas fa-cog"></i>
							</div>
							<div class="menu-text">사용자 관리</div>
						</a>
						<a href="/admin/email/" class="menu-link">
							<div class="menu-icon">
								<i class="fas fa-envelope"></i>
							</div>
							<div class="menu-text">메일내역</div>
						</a>
						<a href="/admin/viewDataFinal" class="menu-link">
							<div class="menu-icon">
								<i class="fas fa-envelope"></i>
							</div>
							<div class="menu-text">매칭현황</div>
						</a>

						
						
						</sec:authorize>
						
						<sec:authorize access="hasAuthority('GOV')">
												 
						<%-- <sec:authorize access="isAuthenticated()"> --%>
						<a href="/member/gov/viewData" class="menu-link">
							<div class="menu-icon">
								<i class="fas fa-address-card"></i>
							</div>
							<div class="menu-text">정보보기</div>
						</a>
						</sec:authorize>
						
						<sec:authorize access="hasAuthority('LD')">
												 
						<%-- <sec:authorize access="isAuthenticated()"> --%>
						<a href="/member/ld/viewData" class="menu-link">
							<div class="menu-icon">
								<i class="fas fa-address-card"></i>
							</div>
							<div class="menu-text">정보보기</div>
						</a>
						</sec:authorize>
						
						<sec:authorize access="hasAuthority('NLD')">
												 
						<%-- <sec:authorize access="isAuthenticated()"> --%>
						<a href="/member/nld/viewData" class="menu-link">
							<div class="menu-icon">
								<i class="fas fa-address-card"></i>
							</div>
							<div class="menu-text">정보보기</div>
						</a>
						</sec:authorize>
					</div>
					<!-- BEGIN minify-button -->
					<div class="menu-item d-flex">
						<a href="javascript:;" class="app-sidebar-minify-btn ms-auto" data-toggle="app-sidebar-minify"><i class="fa fa-angle-double-left"></i></a>
					</div>
					<!-- END minify-button -->
				</div>
				<!-- END menu -->
			</div>
			<!-- END scrollbar -->
		</div>
		<div class="app-sidebar-bg"></div>
		<div class="app-sidebar-mobile-backdrop"><a href="#" data-dismiss="app-sidebar-mobile" class="stretched-link"></a></div>
		<!-- END #sidebar -->