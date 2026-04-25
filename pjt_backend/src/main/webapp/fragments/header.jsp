<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>구해줘 Home</title>
	<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
	<link href="${pageContext.request.contextPath }/css/style.css" rel="stylesheet">
	<link rel="stylesheet" href="https://unpkg.com/leaflet@1.9.4/dist/leaflet.css">

	<script>
		window.ROOT_PATH = "${pageContext.request.contextPath}";
	</script>
	
	<script src="https://unpkg.com/leaflet@1.9.4/dist/leaflet.js"></script>
	<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
	<script src="https://cdn.jsdelivr.net/npm/chart.js"></script>

	<script src="${pageContext.request.contextPath}/js/main.js"></script>
</head>

<body>

	<!-- Navbar -->
	<nav class="navbar navbar-expand-lg navbar-dark bg-dark">
		<div class="container">
			<a class="navbar-brand fw-bold"
				href="${pageContext.request.contextPath}/main?action=index">구해줘
				Home</a>
			<button class="navbar-toggler" type="button"
				data-bs-toggle="collapse" data-bs-target="#navbarContent">
				<span class="navbar-toggler-icon"></span>
			</button>
			<div class="collapse navbar-collapse" id="navbarContent">

				<!-- 비로그인 상태 -->
				<div class="d-flex align-items-center w-100" id="nav-logged-out">
					<ul class="navbar-nav me-auto mb-2 mb-lg-0">
						<li class="nav-item"><a class="nav-link"
							href="pages/notice.html">공지사항</a></li>
						<li class="nav-item"><a class="nav-link" href="#news-section">오늘의
								뉴스</a></li>
					</ul>
					<div class="d-flex gap-2">
						<a href="pages/signup.jsp" class="btn btn-outline-light btn-sm">Sign
							Up</a> <a href="${pageContext.request.contextPath}/pages/login.jsp"
							class="btn btn-outline-light btn-sm">Login</a>
					</div>
				</div>

				<!-- 로그인 상태 -->
				<div class="d-flex align-items-center w-100 d-none"
					id="nav-logged-in">
					<ul class="navbar-nav me-auto mb-2 mb-lg-0">
						<li class="nav-item"><a class="nav-link"
							href="pages/notice.html">공지사항</a></li>
						<li class="nav-item"><a class="nav-link" href="#news-section">오늘의
								뉴스</a></li>
						<li class="nav-item"><a class="nav-link" href="#">자유글</a></li>
						<li class="nav-item"><a class="nav-link"
							href="pages/environment.html">주변탐방</a></li>
						<li class="nav-item"><a class="nav-link"
							href="pages/interest.html">관심지역 설정</a></li>
						<li class="nav-item"><a class="nav-link"
							href="pages/explore.html">관심지역 둘러보기</a></li>
					</ul>
					<div class="d-flex gap-2 align-items-center">
						<span class="text-white small" id="nav-user-name"></span> <a
							href="pages/member.html" class="btn btn-outline-light btn-sm">회원정보</a>
						<button type="button" onclick="logout()" class="btn btn-outline-light btn-sm">Logout</button>
					</div>
				</div>

			</div>
		</div>
	</nav>