// === localStorage 헬퍼 ===
function getUsers() { return JSON.parse(localStorage.getItem('users') || '[]'); }
function saveUsers(users) { localStorage.setItem('users', JSON.stringify(users)); }
function getCurrentUser() { return JSON.parse(localStorage.getItem('currentUser') || 'null'); }
function setCurrentUser(user) { localStorage.setItem('currentUser', JSON.stringify(user)); }
function getNotices() { return JSON.parse(localStorage.getItem('notices') || '[]'); }
function saveNotices(notices) { localStorage.setItem('notices', JSON.stringify(notices)); }
function getInterests() { return JSON.parse(localStorage.getItem('interests') || '[]'); }
function saveInterests(interests) { localStorage.setItem('interests', JSON.stringify(interests)); }

// === 인증 ===
function checkLogin() { return getCurrentUser() !== null; }
function logout() { localStorage.removeItem('currentUser'); location.href = (location.pathname.includes('/pages/')) ? '../index.html' : 'index.html'; }
function requireLogin() { if (!checkLogin()) { alert('로그인이 필요합니다.'); location.href = (location.pathname.includes('/pages/')) ? 'login.html' : 'pages/login.html'; return false; } return true; }

// === Navbar 업데이트 ===
function updateNavbar() {
  const loggedOut = document.getElementById('nav-logged-out');
  const loggedIn = document.getElementById('nav-logged-in');
  if (!loggedOut || !loggedIn) return;
  if (checkLogin()) {
    loggedOut.classList.add('d-none');
    loggedIn.classList.remove('d-none');
    const nameEl = document.getElementById('nav-user-name');
    if (nameEl) nameEl.textContent = getCurrentUser().name + '님';
  } else {
    loggedOut.classList.remove('d-none');
    loggedIn.classList.add('d-none');
  }
}

// === 가격 포맷 ===
function formatPrice(price) {
  const num = typeof price === 'string' ? parseInt(price.replace(/,/g, '')) : price;
  if (num >= 10000) return (num / 10000).toFixed(1) + '억';
  return num.toLocaleString() + '만';
}

// === 지역 데이터 (VWorld 기반 JSON) ===
const dataBasePath = location.pathname.includes('/pages/') ? '../data' : 'data';
let sidoData = [], sigunguData = [], emdData = [];

function loadRegionData() {
  return Promise.all([
    fetch(`${dataBasePath}/sido.json`).then(r => r.json()),
    fetch(`${dataBasePath}/sigungu.json`).then(r => r.json()),
    fetch(`${dataBasePath}/emd.json`).then(r => r.json())
  ]).then(([sido, sigungu, emd]) => {
    sidoData = sido;
    sigunguData = sigungu;
    emdData = emd;
  });
}

function populateSido(selectId) {
  const sel = document.getElementById(selectId);
  if (!sel) return;
  sel.innerHTML = '<option value="">시/도 선택</option>';
  sidoData.forEach(item => {
    const opt = document.createElement('option');
    opt.value = item.code;
    opt.textContent = item.name;
    sel.appendChild(opt);
  });
}

function populateGugun(sidoCode, selectId) {
  const sel = document.getElementById(selectId);
  if (!sel) return;
  sel.innerHTML = '<option value="">시/군/구 선택</option>';
  if (!sidoCode) return;
  sigunguData.filter(item => item.sido === sidoCode).forEach(item => {
    const opt = document.createElement('option');
    opt.value = item.code;
    opt.textContent = item.name;
    sel.appendChild(opt);
  });
}

function populateDong(sidoCode, gugunCode, selectId) {
  const sel = document.getElementById(selectId);
  if (!sel) return;
  sel.innerHTML = '<option value="">읍/면/동 선택</option>';
  if (!gugunCode) return;
  emdData.filter(item => item.sigungu === gugunCode).forEach(item => {
    const opt = document.createElement('option');
    opt.value = item.code;
    opt.textContent = item.name;
    sel.appendChild(opt);
  });
}

// 페이지 로드 시 navbar 업데이트 및 지역 데이터 로드
document.addEventListener('DOMContentLoaded', function() {
  updateNavbar();
  loadRegionData();
});
