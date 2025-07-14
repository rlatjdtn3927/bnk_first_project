  let allFaqs = [];
  let currentMainType = "기업형 DC/DB";
  let currentCategory = "전체";
  let currentPage = 0;

  const categoryMap = {
    "기업형 DC/DB": [
      "전체", "입금(퇴직금 및 부담금)", "상품변경", "디폴트옵션", "현금성자산",
      "수수료", "중도인출", "ETF/펀드", "퇴직연금 제도", "지급"
    ],
    "개인형 IRP": [
      "전체", "입금(퇴직금 및 부담금)", "상품변경", "IRP해지", "디폴트옵션",
      "현금성자산", "IRP신규", "연금", "ETF/펀드", "기타(계약이전 등)"
    ]
  };

  async function loadFaqs(mainType = currentMainType, category = currentCategory, page = 0, keyword = "") {
    currentMainType = mainType;
    currentCategory = category;
    currentPage = page;

    try {
      const params = new URLSearchParams({ mainType, page, size: 10 });
      if (category && category !== "전체") params.append("subType", category);
      if (keyword) params.append("keyword", keyword);

      const response = await fetch(`/api/faq?${params.toString()}`);
      if (!response.ok) throw new Error("데이터 요청 실패");

      const data = await response.json();
      allFaqs = data.content;

      document.getElementById("tab-dcdb").classList.remove("active");
      document.getElementById("tab-irp").classList.remove("active");
      document.getElementById("tab-" + (mainType === '기업형 DC/DB' ? 'dcdb' : 'irp')).classList.add("active");

      const catBox = document.getElementById("category-tabs");
      catBox.className = "sub-tabs";

      catBox.innerHTML = categoryMap[mainType].map(c => `
        <button onclick="loadFaqs('${mainType}', '${c}', 0)" ${c === category ? 'class="active"' : ''}>${c}</button>
      `).join("");

      const faqList = document.getElementById("faq-list");
      faqList.innerHTML = allFaqs.length === 0
        ? "<p>해당 항목에 대한 FAQ가 없습니다.</p>"
        : allFaqs.map((faq, index) => `
            <div class="accordion" onclick="togglePanel(this)">
              Q${index + 1 + page * 10}. ${faq.question}
            </div>
            <div class="panel">${faq.answer}</div>
          `).join("");

      renderPagination(data.totalPages);
    } catch (error) {
      console.error("FAQ 불러오기 실패:", error);
      document.getElementById("faq-list").innerHTML = `<p>FAQ를 불러올 수 없습니다.</p>`;
    }
  }

  function renderPagination(totalPages) {
    const pagination = document.getElementById("pagination");
    if (totalPages <= 1) {
      pagination.innerHTML = '';
      return;
    }

    let html = '';
    if (currentPage > 0) {
      html += `<button onclick="loadFaqs('${currentMainType}', '${currentCategory}', 0)"><<</button>`;
      html += `<button onclick="loadFaqs('${currentMainType}', '${currentCategory}', ${currentPage - 1})"><</button>`;
    } else {
      html += `<button disabled><<</button><button disabled><</button>`;
    }

    const visibleSize = 5;
    const startPage = Math.floor(currentPage / visibleSize) * visibleSize;
    const endPage = Math.min(startPage + visibleSize, totalPages);

    for (let i = startPage; i < endPage; i++) {
      html += `<button onclick="loadFaqs('${currentMainType}', '${currentCategory}', ${i})" ${i === currentPage ? 'class="active"' : ''}>${i + 1}</button>`;
    }

    if (currentPage < totalPages - 1) {
      html += `<button onclick="loadFaqs('${currentMainType}', '${currentCategory}', ${currentPage + 1})">></button>`;
      html += `<button onclick="loadFaqs('${currentMainType}', '${currentCategory}', ${totalPages - 1})">>></button>`;
    } else {
      html += `<button disabled>></button><button disabled>>></button>`;
    }

    pagination.innerHTML = html;
  }

  function searchFaqs() {
    const keyword = document.getElementById("search-input").value.trim();
    loadFaqs(currentMainType, "전체", 0, keyword);
  }

  function togglePanel(el) {
  const panel = el.nextElementSibling;
  const isOpen = panel.classList.contains("open");

  // 다른 열려있는 패널 닫기
  document.querySelectorAll('.panel.open').forEach(p => {
    if (p !== panel) {
      p.classList.remove('open');
      p.style.maxHeight = null;
      p.style.padding = "0 15px";
    }
  });

  if (!isOpen) {
    panel.classList.add('open');
    el.classList.add('open')

    // 줄바꿈 처리
    if (!panel.dataset.processed) {
      panel.innerHTML = panel.innerHTML.replace(/\\n|(\r\n)|(\n)/g, "<br>");
      panel.dataset.processed = "true";
    }

    const dummy = panel.cloneNode(true);
    dummy.style.position = "absolute";
    dummy.style.visibility = "hidden";
    dummy.style.height = "auto";
    dummy.style.maxHeight = "none";
    dummy.style.padding = "15px";
    dummy.style.pointerEvents = "none";
    document.body.appendChild(dummy);

    const fullHeight = dummy.scrollHeight;
    document.body.removeChild(dummy);

    panel.style.maxHeight = fullHeight + "px";
    panel.style.padding = "15px";

  } else {
    panel.classList.remove('open');
    panel.style.maxHeight = null;
    panel.style.padding = "0 15px";
    el.classList.remove('open');
  }
}



  // 초기 로딩
  document.addEventListener("DOMContentLoaded", () => {
    loadFaqs();
  });