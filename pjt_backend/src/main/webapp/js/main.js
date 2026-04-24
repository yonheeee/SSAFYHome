// === 경로 헬퍼 ===
const isInPages = location.pathname.includes('/pages/');
const API_BASE  = isInPages ? '..' : '.';

// === 인증 ===
async function checkLogin() {
  try {
    const res = await fetch(`${API_BASE}/auth?action=check`, {
      headers: { 'X-Requested-With': 'XMLHttpRequest' },
      credentials: 'same-origin'
    });

    return await res.json();
  } catch (e) {
    console.error('로그인 상태 확인 실패:', e);
    return { result: 'fail' };
  }
}

async function logout() {
  try {
    await fetch(`${API_BASE}/auth?action=logout`, {
      method: 'POST',
      headers: { 'X-Requested-With': 'XMLHttpRequest' },
      credentials: 'same-origin'
    });
  } catch (e) {
    console.error('로그아웃 실패:', e);
  }

  location.href = isInPages ? '../index.html' : 'index.html';
}

async function requireLogin() {
  const auth = await checkLogin();

  if (auth.result !== 'success') {
    alert('로그인이 필요합니다.');
    location.href = isInPages ? 'login.html' : 'pages/login.html';
    return false;
  }

  return true;
}

// === Navbar 업데이트 ===
async function updateNavbar() {
  const loggedOut = document.getElementById('nav-logged-out');
  const loggedIn  = document.getElementById('nav-logged-in');
  const nameEl    = document.getElementById('nav-user-name');

  if (!loggedOut || !loggedIn) return;

  const auth = await checkLogin();

  if (auth.result === 'success') {
    // 비로그인 메뉴 숨김
    loggedOut.classList.add('d-none');

    // 로그인 메뉴 표시
    loggedIn.classList.remove('d-none');

    // 혹시 style display가 남아있을 경우 대비
    loggedOut.style.display = 'none';
    loggedIn.style.display = 'flex';

    if (nameEl) {
      nameEl.textContent = auth.name + '님';
    }
  } else {
    // 비로그인 메뉴 표시
    loggedOut.classList.remove('d-none');

    // 로그인 메뉴 숨김
    loggedIn.classList.add('d-none');

    // 혹시 style display가 남아있을 경우 대비
    loggedOut.style.display = 'flex';
    loggedIn.style.display = 'none';

    if (nameEl) {
      nameEl.textContent = '';
    }
  }
}

// === 가격 포맷 ===
function formatPrice(price) {
  const num = typeof price === 'string'
    ? parseInt(price.replace(/,/g, ''), 10)
    : price;

  if (num >= 10000) return (num / 10000).toFixed(1) + '억';
  return num.toLocaleString() + '만';
}

// === 지역 데이터 ===
const dataBasePath = isInPages ? '../data' : 'data';

let sidoData = [];
let sigunguData = [];
let emdData = [];

async function loadRegionData() {
  const [sido, sigungu, emd] = await Promise.all([
    fetch(`${dataBasePath}/sido.json`).then(r => r.json()),
    fetch(`${dataBasePath}/sigungu.json`).then(r => r.json()),
    fetch(`${dataBasePath}/emd.json`).then(r => r.json())
  ]);

  sidoData = sido;
  sigunguData = sigungu;
  emdData = emd;
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

  sigunguData
    .filter(item => item.sido === sidoCode)
    .forEach(item => {
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

  emdData
    .filter(item => item.sigungu === gugunCode)
    .forEach(item => {
      const opt = document.createElement('option');
      opt.value = item.code;
      opt.textContent = item.name;
      sel.appendChild(opt);
    });
}

// === 페이지 로드 ===
document.addEventListener('DOMContentLoaded', async function () {
  await updateNavbar();

  try {
    await loadRegionData();
  } catch (e) {
    console.error('지역 데이터 로딩 실패:', e);
  }
});