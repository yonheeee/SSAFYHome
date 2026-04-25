<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" import="com.ssafy.dto.*"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@include file="/fragments/header.jsp"%>

<div class="container-fluid bg-light py-3 border-bottom">
    <div class="container">
        <div class="row g-2 align-items-end">
            <div class="col-md-3">
                <label class="form-label fw-semibold mb-1">시도</label>
                <select class="form-select" id="sido-select"><option value="">시도 선택</option></select>
            </div>
            <div class="col-md-3">
                <label class="form-label fw-semibold mb-1">시군구</label>
                <select class="form-select" id="gugun-select"><option value="">시군구 선택</option></select>
            </div>
            <div class="col-md-3">
                <label class="form-label fw-semibold mb-1">읍면동</label>
                <select class="form-select" id="dong-select"><option value="">읍면동 선택</option></select>
            </div>
            <div class="col-md-2">
                <label class="form-label fw-semibold mb-1">거래유형</label>
                <select class="form-select" id="deal-type-select">
                    <option value="apt-deal">아파트 매매</option>
                    <option value="apt-rent">아파트 전월세</option>
			        <option value="multi-deal">연립다세대 매매</option>
			        <option value="multi-rent">연립다세대 전월세</option>
                </select>
            </div>
            <div class="col-md-1">
                <button class="btn btn-primary w-100" id="search-btn">조회</button>
            </div>
        </div>
    </div>
</div>

<div class="container-fluid py-3">
    <div class="row g-3">
        <div class="col-md-4">
            <h5 class="fw-bold mb-3">거래 정보</h5>
            <div id="deal-list">
                <c:choose>
                    <c:when test="${not empty houseList}">
                        <div class="list-group">
                            <c:forEach var="house" items="${houseList}">
                                <a href="#" class="list-group-item list-group-item-action" onclick="showDetail('${house.houseName}')">
                                    <div class="d-flex w-100 justify-content-between">
                                        <h6 class="mb-1 fw-bold">${house.houseName}</h6>
                                        <small class="text-primary">${house.dealPrice}만</small>
                                    </div>
                                    <p class="mb-1 small text-muted">${house.dongName} ${house.jibun}</p>
                                </a>
                            </c:forEach>
                        </div>
                    </c:when>
                    <c:otherwise><p class="text-muted">검색 결과가 없습니다.</p></c:otherwise>
                </c:choose>
            </div>
        </div>
        <div class="col-md-8"><div id="map" style="height: 500px; border:1px solid #ccc;"></div></div>
    </div>
</div>

<div class="offcanvas offcanvas-end" tabindex="-1" id="dealOffcanvas">
    <div class="offcanvas-header"><h5 class="offcanvas-title" id="offcanvas-title">상세정보</h5><button type="button" class="btn-close" data-bs-dismiss="offcanvas"></button></div>
    <div class="offcanvas-body" id="offcanvas-body"></div>
</div>

<script>
let map;
let detailOffcanvas;

function initMap() {
	if (map) return;
	
	map = L.map('map').setView([36.3504, 127.3845], 13);
	L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
		attribution: '&copy; OpenStreetMap contributors'
	}).addTo(map);
    
}

function showDetail(houseName) {
    const CTX = window.location.pathname.substring(0, window.location.pathname.indexOf("/", 2));
    
    const dealTypeEl = document.getElementById('deal-type-select');
    const dealType = dealTypeEl ? dealTypeEl.value : "apt-deal";
    
    const url = CTX + "/house?action=detail&houseName=" + encodeURIComponent(houseName) + "&dealType=" + dealType;
    
    console.log("요청 주소:", url);
    
    fetch(url)
        .then(response => {
            if (!response.ok) {
                return response.text().then(errText => {
                    throw new Error("서버 에러 (" + response.status + ")");
                });
            }
            return response.text();
        })
        .then(html => {
        	const body = document.getElementById('offcanvas-body');
            const title = document.getElementById('offcanvas-title');
            
            body.innerHTML = html;
            title.textContent = houseName;
            
            if(!detailOffcanvas) {
                detailOffcanvas = new bootstrap.Offcanvas(document.getElementById('dealOffcanvas'));
            }
            detailOffcanvas.show();
        })
        .catch(err => {
            console.error(err);
            alert("상세 정보를 가져올 수 없습니다. 서버 콘솔이나 파일 경로를 확인해주세요.");
        });
}

document.addEventListener('DOMContentLoaded', async function() {
    const s = "${selectedSido}";
    const g = "${selectedGugun}";
    const d = "${selectedDong}";
    const CTX = window.location.pathname.substring(0, window.location.pathname.indexOf("/", 2));
    
    try {
    	await autoSelectRegion('sido-select', 'gugun-select', 'dong-select', s, g, d);
    } catch(e) {
    	console.error("지역 로드 중 오류", e);
    }
    
    initMap();
    await autoSelectRegion('sido-select', 'gugun-select', 'dong-select', s, g, d);
    
    const sidoSel = document.getElementById('sido-select');
    const gugunSel = document.getElementById('gugun-select');
    const dongSel = document.getElementById('dong-select');
    
    sidoSel.onchange = (e) => {
        const selectedSidoCode = e.target.value;
        populateGugun(selectedSidoCode, 'gugun-select');
        
        dongSel.innerHTML = '<option value="">읍/면/동 선택</option>';
    };
    
    gugunSel.onchange = (e) => {
        const selectedSidoCode = sidoSel.value;
        const selectedGugunCode = e.target.value;
        populateDong(selectedSidoCode, selectedGugunCode, 'dong-select');
    };
    
    const houseList = [];
    <c:forEach var="h" items="${houseList}">
        houseList.push({ name: "${h.houseName}", price: "${h.dealPrice}" });
    </c:forEach>
    
    if(houseList.length > 0 && g) {
		fetch(CTX + '/data/sigungu-coords.json')
			.then(r => r.json())
			.then(coords => {
				const c = coords[g];
				if(c && map) {
					map.setView([c[0], c[1]], 14);
					houseList.forEach(h => {
						const m = L.marker([
							c[0] + (Math.random()-0.5)*0.01,
							c[1] + (Math.random()-0.5)*0.01
						]).addTo(map);
						m.bindPopup("<b>"+h.name+"</b><br>"+h.price+"만");
						m.on('click', function() {
						    showDetail(h.name); // 마커 클릭 시 상세정보 함수 실행!
						});
					});
				}
			});
	}
    
    document.getElementById('search-btn').onclick = () => {
    	if(!dongSel.value) {
    		alert("지역을 모두 선택해주세요.");
    		return;
    	}
    	
        const f = document.createElement('form');
        f.method='POST'; f.action=CTX + '/house';
        
        const params = { 
            action:'search', 
            sido: sidoSel.value, 
            sidoName: sidoSel.options[sidoSel.selectedIndex].text,
            gugun: gugunSel.value,
            gugunName: gugunSel.options[gugunSel.selectedIndex].text,
            dong: dongSel.value,
            dongName: dongSel.options[dongSel.selectedIndex].text,
            dealType: document.getElementById('deal-type-select').value 
        };
        
        for(let k in params) { 
            let i=document.createElement('input'); 
            i.type='hidden'; i.name=k; i.value=params[k]; 
            f.appendChild(i); 
        }
        document.body.appendChild(f); 
        f.submit();
    };
});
</script>

<%@include file="/fragments/footer.jsp"%>
