<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="ko">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>회원 가입 - 구해줘 Home</title>
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
  <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css">
  <link rel="stylesheet" href="../css/style.css">
</head>
<body class="d-flex flex-column min-vh-100">

<nav class="navbar navbar-expand-lg navbar-dark bg-dark">
  <div class="container">
    <a class="navbar-brand fw-bold" href="${pageContext.request.contextPath}/main?action=index">구해줘 Home</a>
    <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarContent">
      <span class="navbar-toggler-icon"></span>
    </button>
    <div class="collapse navbar-collapse" id="navbarContent">
      <div class="d-flex align-items-center w-100" id="nav-logged-out">
        <ul class="navbar-nav me-auto mb-2 mb-lg-0">
          <li class="nav-item"><a class="nav-link" href="notice.html">공지사항</a></li>
          <li class="nav-item"><a class="nav-link" href="../${pageContext.request.contextPath}/main?action=index#news-section">오늘의 뉴스</a></li>
        </ul>
        <div class="d-flex gap-2">
          <a href="signup.jsp" class="btn btn-outline-light btn-sm">Sign Up</a>
          <a href="login.jsp" class="btn btn-outline-light btn-sm">Login</a>
        </div>
      </div>
      <div class="d-flex align-items-center w-100" id="nav-logged-in" style="display:none">
        <ul class="navbar-nav me-auto mb-2 mb-lg-0">
          <li class="nav-item"><a class="nav-link" href="notice.html">공지사항</a></li>
          <li class="nav-item"><a class="nav-link" href="../${pageContext.request.contextPath}/main?action=index#news-section">오늘의 뉴스</a></li>
          <li class="nav-item"><a class="nav-link" href="#">자유글</a></li>
          <li class="nav-item"><a class="nav-link" href="environment.html">주변탐방</a></li>
          <li class="nav-item"><a class="nav-link" href="interest.html">관심지역 설정</a></li>
          <li class="nav-item"><a class="nav-link" href="explore.html">관심지역 둘러보기</a></li>
        </ul>
        <div class="d-flex gap-2 align-items-center">
          <span class="text-white small" id="nav-user-name"></span>
          <a href="member.html" class="btn btn-outline-light btn-sm">회원정보</a>
          <button onclick="logout()" class="btn btn-outline-light btn-sm">Logout</button>
        </div>
      </div>
    </div>
  </div>
</nav>

<main class="bg-light flex-grow-1 py-5">
  <div class="container">
    <nav aria-label="breadcrumb" class="mb-4">
      <ol class="breadcrumb">
        <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/main?action=index">Home</a></li>
        <li class="breadcrumb-item active">회원 가입</li>
      </ol>
    </nav>

    <div class="card mx-auto shadow-sm" style="max-width:500px">
      <div class="card-body p-4">
        <h3 class="mb-4">회원 가입</h3>
        <form id="signup-form" novalidate>
          <div class="input-group mb-3">
            <span class="input-group-text"><i class="bi bi-person"></i></span>
            <input type="text" class="form-control" id="userId" placeholder="아이디" required>
          </div>
          <div class="input-group mb-3">
            <span class="input-group-text"><i class="bi bi-lock"></i></span>
            <input type="password" class="form-control" id="userPw" placeholder="비밀번호 (영문 숫자 조합/4자 이상)" required>
          </div>
          <div class="input-group mb-3">
            <span class="input-group-text"><i class="bi bi-person-badge"></i></span>
            <input type="text" class="form-control" id="userName" placeholder="이름" required>
          </div>
          <div class="input-group mb-3">
            <span class="input-group-text"><i class="bi bi-house"></i></span>
            <input type="text" class="form-control" id="userAddr" placeholder="주소">
          </div>
          <div class="input-group mb-4">
            <span class="input-group-text"><i class="bi bi-phone"></i></span>
            <input type="tel" class="form-control" id="userPhone" placeholder="전화번호 (010-xxxx-xxxx)" required>
          </div>
          <div class="d-grid">
            <button type="submit" class="btn btn-warning">
              <i class="bi bi-check-lg me-1"></i>등록
            </button>
          </div>
        </form>
      </div>
    </div>
  </div>
</main>

<footer class="bg-dark text-white py-4">
	<div class="container">
		<div class="row">
			<div class="col-md-4">
				<h5>Contact Us</h5>
				<p class="mb-1">(34153) 대전 유성구 동서대로 98-39</p>
				<p class="mb-1">042-820-7400</p>
				<p class="mb-0">ssafy@ssafy.com</p>
			</div>
			<div
				class="col-md-8 d-flex align-items-center justify-content-md-end">
				<p class="mb-0 text-muted">&copy; 2026 구해줘 Home. All rights reserved.</p>
			</div>
		</div>
	</div>
</footer>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
<script src="${pageContext.request.contextPath}/js/main.js"></script>
<script>
document.getElementById('signup-form').addEventListener('submit', async function(e) {
  e.preventDefault();

  const id = document.getElementById('userId').value.trim();
  const password = document.getElementById('userPw').value.trim();
  const name = document.getElementById('userName').value.trim();
  const address = document.getElementById('userAddr').value.trim();
  const phone = document.getElementById('userPhone').value.trim();

  if (!id || !password || !name || !phone) {
    alert('필수 항목을 모두 입력해주세요.');
    return;
  }

  const pwRegex = /^(?=.*[a-zA-Z])(?=.*\d).{4,}$/;
  if (!pwRegex.test(password)) {
    alert('비밀번호는 영문과 숫자를 조합하여 4자 이상 입력해주세요.');
    return;
  }

  try {
    const response = await fetch('../member?action=register', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/x-www-form-urlencoded;charset=UTF-8',
        'X-Requested-With': 'XMLHttpRequest'
      },
      body: new URLSearchParams({
        id,
        password,
        name,
        address,
        phone
      })
    });

    const json = await response.json();

    if (json.result === 'success') {
      alert('회원가입이 완료되었습니다.');
      location.href = 'login.jsp';
    } else {
      alert(json.message || '회원가입에 실패했습니다.');
    }
  } catch (error) {
    console.error(error);
    alert('회원가입 요청 중 오류가 발생했습니다.');
  }
});
</script>
</body>
</html>
