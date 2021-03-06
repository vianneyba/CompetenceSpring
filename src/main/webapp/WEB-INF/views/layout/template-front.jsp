<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html lang="fr">
<head>
	<meta charset="UTF-8">
	<meta name="viewport" content="width=device-width, initial-scale=1">
	<title>Quiz - <sitemesh:write property='title' /></title>
	<!-- Bootsrap CSS -->
	<link href="https://stackpath.bootstrapcdn.com/bootstrap/4.1.3/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-MCw98/SFnGE8fJT3GXwEOngsV7Zt27NXFoaoApmYm81iuXoPkFOJwJ8ERdknLPMO" crossorigin="anonymous">
	<link rel="stylesheet" href="https://use.fontawesome.com/releases/v5.4.2/css/all.css" integrity="sha384-/rXc/GQVaYpyDdyxK+ecHPVYJSN9bmVFBvjA/9eOB+pb3F2w2N6fc5qB9Ew5yIns" crossorigin="anonymous">
	<!-- Jquery CDN -->
	<script src="https://code.jquery.com/jquery-3.3.1.min.js" integrity="sha256-FgpCb/KJQlLNfOu91ta32o/NMZxltwRo8QtmkMRdAu8=" crossorigin="anonymous"></script>
	<!-- Bootstrap JS -->
	<script src="https://stackpath.bootstrapcdn.com/bootstrap/4.1.3/js/bootstrap.min.js" integrity="sha384-ChfqqxuZUCnJSK3+MXmPNIyE6ZbWh2IMqE241rYiqJxyMiZ6OW/JmZQ5stwEULTy" crossorigin="anonymous"></script>
</head>
<body>
	<%@include file="/WEB-INF/views/layout/navbar.jsp" %>
	<div class="container">
<!-- 		<div class="row"> -->
<!-- 			<aside class="md-col-3"> -->
<%-- 				<c:choose> --%>
<%-- 					<c:when test="${ user.type == 'PROFESSOR' }"> --%>
<%-- 						<%@include file="/WEB-INF/views/layout/asideProfessor.jsp" %> --%>
<%-- 					</c:when> --%>
<%-- 					<c:when test="${ user.type == 'STUDENT' }"> --%>
<%-- 						<%@include file="/WEB-INF/views/layout/asideStudent.jsp" %> --%>
<%-- 					</c:when> --%>
<%-- 				</c:choose> --%>
<!-- 			</aside> -->
			
			<div class="col-md-12">
				<sitemesh:write property='body'/>
			</div>
		</div>
<!-- 	</div> -->
</body>
</html>