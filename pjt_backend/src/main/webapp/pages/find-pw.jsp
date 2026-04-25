<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="ko">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>비밀번호 찾기 - 구해줘 Home</title>
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body class="bg-light">

<div class="container py-5">
  <div class="card mx-auto shadow-sm" style="max-width:420px">
    <div class="card-body p-4">
      <h3 class="mb-4">비밀번호 찾기</h3>

      <form id="find-pw-form">
        <div class="mb-3">
          <label class="form-label" for="findId">아이디</label>
          <input type="text" class="form-control" id="findId" required>
        </div>

        <div class="mb-3">
          <label class="form-label" for="findPhone">전화번호</label>
          <input type="tel" class="form-control" id="findPhone" placeholder="010-xxxx-xxxx" required>
        </div>

        <div class="d-grid">
          <button type="submit" class="btn btn-primary">비밀번호 찾기</button>
        </div>
      </form>

      <div class="mt-3">
        <a href="${pageContext.request.contextPath}/pages/login.jsp">로그인으로 돌아가기</a>
      </div>
    </div>
  </div>
</div>

<script>
document.getElementById('find-pw-form').addEventListener('submit', async function(e) {
  e.preventDefault();

  const id = document.getElementById('findId').value.trim();
  const phone = document.getElementById('findPhone').value.trim();

  if (!id || !phone) {
    alert('아이디와 전화번호를 입력해주세요.');
    return;
  }

  try {
    const response = await fetch('../auth?action=findPassword', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/x-www-form-urlencoded;charset=UTF-8',
        'X-Requested-With': 'XMLHttpRequest'
      },
      body: new URLSearchParams({ id, phone }),
      credentials: 'same-origin'
    });

    const json = await response.json();

    if (json.result === 'success') {
      alert(`회원님의 비밀번호는 ${json.password} 입니다.`);
      location.href = 'login.jsp';
    } else {
      alert(json.message || '일치하는 회원 정보가 없습니다.');
    }

  } catch (error) {
    alert('비밀번호 찾기 요청 중 오류가 발생했습니다.');
  }
});
</script>

</body>
</html>