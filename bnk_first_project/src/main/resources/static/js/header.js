document.addEventListener("DOMContentLoaded", () => {
  const navItems = document.querySelectorAll('.nav-item');
  const submenuBox = document.querySelector('.submenu-box');
  const submenus = document.querySelectorAll('.submenu-box .submenu');
  const hamburgerBtn = document.querySelector('.hamburger-menu');

  const desktopSitemap = document.querySelector('.desktop-sitemap');
  const desktopCloseBtn = document.querySelector('.desktop-sitemap .sitemap-close');

  const mobileSitemap = document.querySelector('.mobile-sitemap');
  const mobileCloseBtn = document.querySelector('.mobile-sitemap .sitemap-close');

  const accordionTitles = document.querySelectorAll('.accordion-title');

  let isHoverBound = false;

  const bindPcHoverEvents = () => {
    if (isHoverBound) return;
    isHoverBound = true;

    navItems.forEach(item => {
      item.addEventListener('mouseenter', () => {
        const menuId = item.dataset.menu;
        const targetMenu = document.getElementById(menuId);

        submenuBox.classList.add('active');
        submenus.forEach(ul => ul.classList.remove('active'));

        if (targetMenu) {
          targetMenu.classList.add('active');
        }
      });
    });

    document.querySelector('.main-header').addEventListener('mouseleave', () => {
      submenuBox.classList.remove('active');
      submenus.forEach(ul => ul.classList.remove('active'));
    });
  };

  // 초기 바인딩
  if (window.innerWidth > 768) {
    bindPcHoverEvents();
  }

  // 햄버거 버튼 클릭 시
  hamburgerBtn.addEventListener('click', () => {
    if (window.innerWidth > 768) {
      desktopSitemap?.classList.add('active');
    } else {
      mobileSitemap?.classList.add('active');
    }
  });

  desktopCloseBtn?.addEventListener('click', () => {
    desktopSitemap?.classList.remove('active');
  });

  mobileCloseBtn?.addEventListener('click', () => {
    mobileSitemap?.classList.remove('active');
  });

  // 모바일 아코디언 토글
  accordionTitles.forEach(title => {
    title.addEventListener('click', () => {
      const content = title.nextElementSibling;
      content.classList.toggle('active');
    });
  });

  // 창 크기 변경 시 메뉴 초기화 + 이벤트 재설정
  window.addEventListener('resize', () => {
    const isPC = window.innerWidth > 768;

    // 메뉴 초기화
    submenuBox.classList.remove('active');
    submenus.forEach(ul => ul.classList.remove('active'));
    mobileSitemap.classList.remove('active');
    desktopSitemap.classList.remove('active');

    document.querySelectorAll('.accordion-content').forEach(el => {
      el.classList.remove('active');
    });

    // PC용 hover 이벤트 재설정
    if (isPC) {
      bindPcHoverEvents();
    }
  });
});
