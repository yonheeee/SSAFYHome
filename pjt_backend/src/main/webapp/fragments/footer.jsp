<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

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
				<p class="mb-0 text-muted">&copy; 2026 구해줘 Home. All rights
					reserved.</p>
			</div>
		</div>
	</div>
</footer>

<script>
		function heroSearch() {
			const form = document.getElementById('search-form')
			const sido = document.getElementById('hero-sido');
			const gugun = document.getElementById('hero-gugun');
			const dong = document.getElementById('hero-dong');

			if (!sido.value || !gugun.value || !dong.value) {
				alert("지역을 모두 선택해주세요.");
				return;
			}

			const sidoName = sido.options[sido.selectedIndex].text;
			const gugunName = gugun.options[gugun.selectedIndex].text;
			const dongName = dong.options[dong.selectedIndex].text;

			document.getElementById('hidden-sido-name').value = sidoName;
			document.getElementById('hidden-gugun-name').value = gugunName;
			document.getElementById('hidden-dong-name').value = dongName;

			form.submit();
		}
</script>
</body>
</html>