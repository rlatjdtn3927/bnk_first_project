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

  modal.style.display = 'none';
  bg.style.display = 'none';

  btn.onclick = () => {
    modal.style.display = 'flex';
    bg.style.display = 'block';
    input.focus();
  };

  close.onclick = bg.onclick = () => {
    modal.style.display = 'none';
    bg.style.display = 'none';
  };

  document.addEventListener('keydown', (e) => {
    if (e.key === 'Escape') {
      modal.style.display = 'none';
      bg.style.display = 'none';
    }
  });

  form.onsubmit = async e => {
    e.preventDefault();
    const q = input.value.trim();
    if (!q) return;

    box.innerHTML += `<div style="text-align:right;margin:7px 0;">
        <span style="background:#dbe5ff;color:#21225a;padding:8px 13px;border-radius:11px;display:inline-block;">
          ${q}
        </span></div>`;
    input.value = '';

    const container = document.createElement("div");
    container.style = "text-align:left;margin:7px 0;";
    const bubble = document.createElement("span");
    bubble.style = "background:#f2f2f2;color:#3e3d3a;padding:8px 13px;border-radius:11px;display:inline-block;white-space:pre-wrap;";
    bubble.innerHTML = "<span style='color:#7a73b6'>AI</span> ";
    container.appendChild(bubble);
    box.appendChild(container);
    box.scrollTop = box.scrollHeight;

    try {
      // 🔍 자동 모드 감지
      const detectMode = msg => {
        const lower = msg.toLowerCase();
        const keywords = ["etf", "tdf", "퇴직연금", "펀드", "수수료", "추천", "비교", "상품"];
        return keywords.some(k => lower.includes(k)) ? "context" : "simple";
      };
      const mode = detectMode(q);

      const r = await fetch('/chat', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ message: q, mode: mode })
      });

      const raw = await r.text();
      const json = JSON.parse(raw);
      let response = json.response || raw;

      response = response.replace(/\*\*/g, '');

      let i = 0;
      const type = () => {
        if (i < response.length) {
          bubble.innerHTML += response[i] === '\n' ? "<br>" : response[i];
          i++;
          setTimeout(type, 10);
          box.scrollTop = box.scrollHeight;
        }
      };
      type();
    } catch (err) {
      bubble.innerHTML = `<span style="color:red;">[에러] 답변을 불러오지 못했습니다.</span>`;
    }
  };
});
