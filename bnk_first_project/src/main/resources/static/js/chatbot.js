// static/js/chatbot.js
const $ = id => document.getElementById(id);

const btn   = $('chatbot-floating-btn');
const modal = $('chatbot-modal');
const bg    = $('chatbot-modal-bg');
const close = $('chatbot-close-btn');
const form  = $('chatbot-form');
const input = $('chatbot-input');
const box   = $('chatbot-messages');

btn.onclick   = () => { modal.style.display='flex'; bg.style.display='block'; input.focus(); };
close.onclick = bg.onclick = () => { modal.style.display='none'; bg.style.display='none'; };

form.onsubmit = async e=>{
  e.preventDefault();
  const q = input.value.trim();
  if(!q) return;
  // 사용자 질문 출력
  box.innerHTML += `<div style="text-align:right;margin:7px 0;">
      <span style="background:#dbe5ff;color:#21225a;padding:8px 13px;border-radius:11px;display:inline-block;">
        ${q}
      </span></div>`;
  input.value='';

  // 로딩 풍선
  box.innerHTML += `<div style="text-align:left;margin:7px 0;" id="loading">
      <span style="background:#f2f2f2;color:#3e3d3a;padding:8px 13px;border-radius:11px;display:inline-block;">
        <span style="color:#7a73b6">AI</span> ... 답변 생성 중
      </span></div>`;
  box.scrollTop = box.scrollHeight;

  try{
     const r = await fetch('/api/chatbot', {
         method:'POST',
         headers:{'Content-Type':'application/json'},
         body:JSON.stringify({question:q})
     });
     const ans = await r.text();
     $('loading').outerHTML = `<div style="text-align:left;margin:7px 0;">
         <span style="background:#f2f2f2;color:#3e3d3a;padding:8px 13px;border-radius:11px;display:inline-block;">
           <span style="color:#7a73b6">AI</span> ${ans.replace(/\n/g,'<br>')}
         </span></div>`;
  }catch(err){
     $('loading').outerHTML = `<div style="color:red;">[에러] 답변을 불러오지 못했습니다.</div>`;
  }
  box.scrollTop = box.scrollHeight;
};
