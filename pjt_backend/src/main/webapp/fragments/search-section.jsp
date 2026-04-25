<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<!-- Hero Section -->
<section class="hero-section">
	<form id="search-form" action="${pageContext.request.contextPath}/house" method="POST">
		<input type="hidden" name="action" value="search-form">
		<input type="hidden" name="sidoName" id="hidden-sido-name">
		<input type="hidden" name="gugunName" id="hidden-gugun-name">
		<input type="hidden" name="dongName" id="hidden-dong-name">
		<input type="hidden" name="dealType" value="apt-deal">
		
		<div class="hero-overlay"></div>
		<div class="container text-center">
			<h1 class="display-4 fw-bold mb-3">구해줘 HOME!!</h1>
			<p class="lead mb-5">우리를 위한 집</p>
			
			<div class="row justify-content-center g-2">
				<div class="col-md-4 col-sm-12">
					<select class="form-select" id="hero-sido" name="sido"
						onchange="populateGugun(this.value,'hero-gugun');">
						<option value="">시/도 선택</option>
					</select>
				</div>
				<div class="col-md-4 col-sm-12">
					<select class="form-select" id="hero-gugun" name="gugun"
						onchange="populateDong(document.getElementById('hero-sido').value, this.value, 'hero-dong')">
						<option value="">시/군/구 선택</option>
					</select>
				</div>
				<div class="col-md-4 col-sm-12">
					<select class="form-select" id="hero-dong" name="dong">
						<option value="">읍/면/동 선택</option>
					</select>
				</div>
				<div class="col-12 mt-3">
					<button type="button" class="btn btn-primary btn-lg px-5" onclick="heroSearch()">조회</button>
				</div>
			</div>
			
		</div>
	</form>
</section>

<script>
document.addEventListener('DOMContentLoaded', function() {
    autoSelectRegion('hero-sido', 'hero-gugun', 'hero-dong', '', '', '');
});
</script>