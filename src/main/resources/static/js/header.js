// BNK 퇴직연금 – 헤더 스크립트 (rev 8)
// ▸ 데스크톱: nav hover → submenu, 햄버거 → 그리드 sitemap
// ▸ 모바일  : 햄버거 → 아코디언 sitemap
// ▸ Resize 시 sitemap 열린 상태 유지 & 타입 자동 전환
// ---------------------------------------------------------
window.addEventListener("DOMContentLoaded", () => {
  const DESKTOP_MIN = 769;

  const body        = document.body;
  const hamburger   = document.querySelector(".hamburger-menu");
  const desktopMap  = document.querySelector(".desktop-sitemap");
  const mobileMap   = document.querySelector(".mobile-sitemap");
  const closeBtns   = document.querySelectorAll(".sitemap-close");
  const navItems    = document.querySelectorAll(".main-nav .nav-item");
  const submenus    = document.querySelectorAll(".submenu");
  const submenuBox  = document.querySelector(".submenu-box");

  /* ---------------- Helpers ---------------- */
  const hasSub = (item) => {
    const id = item.dataset.menu;
    const t  = document.getElementById(id);
    return t && t.children.length > 0;
  };

  const openSitemap = () => {
    body.classList.add("sitemap-open", "no-scroll");
    // 햄버거 숨기기 (CSS: body.sitemap-open .hamburger-menu{display:none})

    if (window.innerWidth >= DESKTOP_MIN) {
      desktopMap.classList.add("open");
      mobileMap.classList.remove("open");
    } else {
      mobileMap.classList.add("open");
      desktopMap.classList.remove("open");
    }
  };

  const closeSitemap = () => {
    body.classList.remove("sitemap-open", "no-scroll");
    desktopMap.classList.remove("open");
    mobileMap.classList.remove("open");
  };

  /* ---------------- Events ---------------- */
  hamburger.addEventListener("click", openSitemap);
  closeBtns.forEach(btn => btn.addEventListener("click", closeSitemap));

  // Resize: sitemap가 열려 있으면 타입만 전환 (닫지 않음)
  window.addEventListener("resize", () => {
    if (!body.classList.contains("sitemap-open")) return;
    if (window.innerWidth >= DESKTOP_MIN) {
      desktopMap.classList.add("open");
      mobileMap.classList.remove("open");
    } else {
      mobileMap.classList.add("open");
      desktopMap.classList.remove("open");
    }
  });

  // 데스크톱 hover submenu (nav)
  navItems.forEach(item => {
    item.addEventListener("mouseenter", () => {
      if (window.innerWidth < DESKTOP_MIN) return;

      // 조건: submenu가 있을 때만 표시
      const id      = item.dataset.menu;
      const target  = document.getElementById(id);
      submenus.forEach(sm => sm.classList.remove("active"));
      submenuBox.classList.remove("show");

      if (target && target.children.length) {
        target.classList.add("active");
        submenuBox.classList.add("show");
      }
    });
  });

  // nav 영역 벗어나면 submenu 닫기
  document.querySelector(".main-header").addEventListener("mouseleave", () => {
    if (window.innerWidth < DESKTOP_MIN) return;
    submenuBox.classList.remove("show");
    submenus.forEach(sm => sm.classList.remove("active"));
  });

  /* 모바일 아코디언 */
  mobileMap.querySelectorAll(".accordion-item:not(.no-sub) > .accordion-title").forEach(title => {
    title.addEventListener("click", () => {
      const item = title.parentElement;
      item.classList.toggle("open");
    });
  });
});