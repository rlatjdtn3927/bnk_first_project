// static/js/chatbot.js
document.addEventListener('DOMContentLoaded', () => {
  const $ = id => document.getElementById(id);

  const btn   = $('chatbot-floating-btn');
  const modal = $('chatbot-modal');
  const bg    = $('chatbot-modal-bg');
  const close = $('chatbot-close-btn');
  const form  = $('chatbot-form');
  const input = $('chatbot-input');
  const box   = $('chatbot-messages');

  if (!btn || !modal || !bg || !close || !form || !input || !box) {
    console.warn('챗봇 관련 DOM 요소를 찾을 수 없습니다.');
    return;
  }

  // 🔻 처음에는 모달과 배경을 숨긴 상태 유지
  modal.style.display = 'none';
  bg.style.display = 'none';

  // 🔻 챗봇 버튼 클릭 시 모달 오픈
  btn.onclick = () => {
    modal.style.display = 'flex';
    bg.style.display = 'block';
    input.focus();
  };

  // 🔻 닫기 버튼 또는 배경 클릭 시 모달 닫기
  close.onclick = bg.onclick = () => {
    modal.style.display = 'none';
    bg.style.display = 'none';
  };

  // 🔻 ESC 키로도 모달 닫기
  document.addEventListener('keydown', (e) => {
    if (e.key === 'Escape') {
      modal.style.display = 'none';
      bg.style.display = 'none';
    }
  });

  // 🔻 챗봇 폼 제출 시 메시지 전송 처리
  // 🔻 챗봇 폼 제출 시 메시지 전송 처리
  form.onsubmit = async e => {
    e.preventDefault();
    const q = input.value.trim();
    if (!q) return;

    // 🔸 사용자 질문 출력
    box.innerHTML += `<div style="text-align:right;margin:7px 0;">
        <span style="background:#dbe5ff;color:#21225a;padding:8px 13px;border-radius:11px;display:inline-block;">
          ${q}
        </span></div>`;
    input.value = '';

    // 🔸 로딩 메시지 자리 만들기
    const container = document.createElement("div");
    container.style = "text-align:left;margin:7px 0;";
    const bubble = document.createElement("span");
    bubble.style = "background:#f2f2f2;color:#3e3d3a;padding:8px 13px;border-radius:11px;display:inline-block;white-space:pre-wrap;";
    bubble.innerHTML = "<span style='color:#7a73b6'>AI</span> ";
    container.appendChild(bubble);
    box.appendChild(container);
    box.scrollTop = box.scrollHeight;

	try {
	  const r = await fetch('/chat', {
	    method: 'POST',
	    headers: { 'Content-Type': 'application/json' },
	    body: JSON.stringify({ message: q })
	  });

	  const raw = await r.text();

	  // 🔸 JSON 응답에서 response 추출
	  const json = JSON.parse(raw);
	  let response = json.response || raw;

	  // 🔸 마크다운 굵게(**) 제거
	  response = response.replace(/\*\*/g, '');

	  // 🔸 타자 효과 출력
	  let i = 0;
	  const type = () => {
	    if (i < response.length) {
	      bubble.innerHTML += response[i] === '\n' ? "<br>" : response[i];
	      i++;
	      setTimeout(type, 10);  // ← 속도 조절 가능
	      box.scrollTop = box.scrollHeight;
	    }
	  };
	  type();
	} catch (err) {
	  bubble.innerHTML = `<span style="color:red;">[에러] 답변을 불러오지 못했습니다.</span>`;
	}

  };

});
