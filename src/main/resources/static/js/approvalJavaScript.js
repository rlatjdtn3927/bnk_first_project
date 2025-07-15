//로그아웃 기능
function logout() {
	fetch('/member/logout')
		.then(response => response.text())
		.then(data => {
			alert('로그아웃 되었습니다!');
			location.href = 'loginForm'
		})
		.catch(err => {
			alert('오류발생!(문의:(내선) 6241)');
		});
}

// --- DOM Elements --- (HTML 구조에 맞게 수정)
const topMenu = document.querySelector('.topMenu'); // HTML에서는 class="topMenu"
const sidebarMenu = document.getElementById('sidebarMenu'); // HTML에서는 id="sidebarMenu"  
const mainContentArea = document.getElementById('main-content-area'); // HTML에서는 id="main-content-area"

// 소분류 매뉴 배열
const submenus = {
	'approval': ['결재 문서 조회', '결재 대기 목록', '처리된 문서'],
	'notice': ['공지사항 목록', '공지사항 등록'],
	'product': ['상품 목록', '상품 등록'],
	'report': ['일일 보고서', '주간 보고서', '월간 보고서']
};

//공지사항 조회
function showNoticeList() {
	console.log("공지사항 목록 조회 시작");

	fetch('/notice/getNoticeList')
		.then(response => {
			console.log("공지사항 목록 응답 상태:", response.status);
			if (!response.ok) {
				throw new Error(`HTTP error! status: ${response.status}`);
			}
			return response.json();
		})
		.then(data => {
			console.log("공지사항 목록 데이터:", data);

			if (!Array.isArray(data)) {
				console.error("받은 데이터가 배열이 아닙니다:", typeof data);
				throw new Error("서버에서 잘못된 형식의 데이터를 받았습니다.");
			}

			console.log("공지사항 개수:", data.length);

			let tableRows = data.map(notice => {
				return `
                    <tr>
                        <td>${notice.b_id || '-'}</td>
                        <td>
                            <a href="#" onclick="viewNoticeContent(${notice.b_id})" 
                               style="color: #007bff; text-decoration: none;">
                                ${notice.b_title || '제목 없음'}
                            </a>
                        </td>
                        <td>${notice.b_view || 0}</td>
                        <td>
                            <button onclick="viewNoticeContent(${notice.b_id})" 
                                    style="padding: 5px 10px; border: 1px solid #007bff; 
                                           background: #007bff; color: white; border-radius: 3px; cursor: pointer;">
                                보기
                            </button>
                        </td>
                    </tr>
                `;
			}).join('');

			const finalHTML = `
                <div class="approval-section">
                    <h2>공지사항 목록</h2>
                    <div style="margin-bottom: 15px; display: flex; justify-content: space-between; align-items: center;">
                        <small style="color: #666;">총 ${data.length}개의 공지사항</small>
                        <button onclick="showUserNoticeForm()" 
                                style="padding: 8px 15px; background: #28a745; color: white; 
                                       border: none; border-radius: 5px; cursor: pointer;">
                            새 공지사항 작성
                        </button>
                    </div>
                    <table class="notice-list-table">
                        <thead>
                            <tr>
                                <th style="width: 80px;">번호</th>
                                <th>제목</th>
                                <th style="width: 100px;">조회수</th>
                                <th style="width: 100px;">작업</th>
                            </tr>
                        </thead>
                        <tbody>
                            ${tableRows.length > 0 ? tableRows : '<tr><td colspan="4">등록된 공지사항이 없습니다.</td></tr>'}
                        </tbody>
                    </table>
                </div>
            `;

			console.log("공지사항 목록 HTML 생성 완료");
			mainContentArea.innerHTML = finalHTML;
		})
		.catch(err => {
			console.error("공지사항 목록 조회 에러:", err);
			alert('공지사항 목록 조회 중 오류가 발생했습니다: ' + err.message);

			mainContentArea.innerHTML = `
                <div class="approval-section">
                    <h2>공지사항 목록</h2>
                    <div style="color: red; padding: 20px; border: 1px solid red; border-radius: 5px;">
                        공지사항 목록 로딩 실패: ${err.message}
                    </div>
                </div>
            `;
		});
}

// 공지사항 상세 보기 함수
function viewNoticeContent(b_id) {
	console.log("공지사항 상세 보기 - ID:", b_id);

	fetch('/notice/getNoticeList')
		.then(response => {
			if (!response.ok) {
				throw new Error(`HTTP error! status: ${response.status}`);
			}
			return response.json();
		})
		.then(data => {
			// 해당 ID의 공지사항 찾기
			const notice = data.find(item => item.b_id == b_id);

			if (!notice) {
				throw new Error('해당 공지사항을 찾을 수 없습니다.');
			}

			console.log('찾은 공지사항:', notice);

			mainContentArea.innerHTML = `
                <div class="approval-section">
                    <div style="margin-bottom: 20px;">
                        <button onclick="showNoticeList()" 
                                style="padding: 8px 15px; background: #6c757d; color: white; 
                                       border: none; border-radius: 5px; cursor: pointer;">
                            ← 목록으로 돌아가기
                        </button>
                    </div>
                    
                    <h2>공지사항 상세</h2>
                    
                    <div class="form-group">
                        <h3>제목</h3>
                        <div style="padding: 10px; border: 1px solid #ddd; border-radius: 5px; 
                                    background-color: #f8f9fa; font-weight: bold;">
                            ${notice.b_title || '제목 없음'}
                        </div>
                    </div>
                    
                    <div class="form-group">
                        <h3>내용</h3>
						<div style="padding: 8px; border: 1px solid #ddd; border-radius: 5px;
						            background-color: #f8f9fa; min-height: 200px;">
						    ${notice.b_content || '내용이 없습니다.'}
						</div>

                    </div>
                    
                    <div style="display: flex; justify-content: space-between; align-items: center; 
                                padding: 10px; background-color: #e9ecef; border-radius: 5px;">
                        <small style="color: #666;">
                            공지사항 번호: ${notice.b_id} | 조회수: ${notice.b_view || 0}
                        </small>
                    </div>
                    
                    <div class="actions" style="margin-top: 20px;">
                        <button onclick="showNoticeList()" 
                                style="padding: 10px 20px; background: #007bff; color: white; 
                                       border: none; border-radius: 5px; cursor: pointer;">
                            목록으로
                        </button>
                    </div>
                </div>
            `;
		})
		.catch(err => {
			console.error('공지사항 상세 조회 에러:', err);
			alert('공지사항 상세 조회 실패: ' + err.message);
		});
}

// --- View Render Functions ---

function showUserNoticeForm() {
	mainContentArea.innerHTML = `
            <div class="approval-section">
                <h2>신규 공지사항 작성</h2>
                <div class="form-group">
                    <input type="text" name="title" placeholder="제목 입력">
                </div>
                <div class="form-group">
                    <textarea name="content" placeholder="내용 입력"></textarea>
                </div>
                <div class="form-group">
                    <input type="text" name="admin_comment" placeholder="작성자 의견">
                </div>
                <div class="actions">
                    <button id="user-approval-btn">등록요청</button>
                </div>
            </div>
        `;
	document.getElementById('user-approval-btn').addEventListener('click', userApprovalFunction);
}

function showPendingNoticeList() {
	console.log("결재 대기 목록 조회 시작");

	fetch('/notice/getPendingList')
		.then(response => {
			console.log("서버 응답 상태:", response.status);
			if (!response.ok) {
				throw new Error(`HTTP error! status: ${response.status}`);
			}
			return response.json();
		})
		.then(data => {
			console.log("서버에서 받은 전체 데이터:", data);

			if (!Array.isArray(data)) {
				console.error("받은 데이터가 배열이 아닙니다:", typeof data);
				throw new Error("서버에서 잘못된 형식의 데이터를 받았습니다.");
			}

			// 결재대기 상태인 항목만 필터링
			const pendingOnlyNotices = data.filter(item => {
				const status = item.status ? item.status.toString().trim() : '';
				const isPending = status === '결재대기';
				console.log(`ID ${item.p_id}: 상태="${status}" -> 결재대기 여부: ${isPending}`);
				return isPending;
			});

			console.log("필터링된 결재대기 항목:", pendingOnlyNotices);
			console.log("결재대기 항목 개수:", pendingOnlyNotices.length);

			let tableRows = pendingOnlyNotices.map(notice => {
				return `
                    <tr>
                        <td>${notice.p_id || '-'}</td>
                        <td>${notice.b_title || '-'}</td>
                        <td>${notice.b_created_at || '-'}</td>
                        <td>
                            <span class="status status-결재대기">결재대기</span>
                        </td>
                        <td>
                            <button onclick="viewNoticeDetail(${notice.p_id})">보기</button>
                        </td>
                    </tr>
                `;
			}).join('');

			const finalHTML = `
                <div class="approval-section">
                    <h2>결재 대기 목록</h2>
                    <div style="margin-bottom: 10px;">
                        <small>총 ${pendingOnlyNotices.length}개의 결재 대기 항목</small>
                    </div>
                    <table class="notice-list-table">
                        <thead>
                            <tr>
                                <th>결재 ID</th>
                                <th>제목</th>
                                <th>작성일</th>
                                <th>상태</th>
                                <th>작업</th>
                            </tr>
                        </thead>
                        <tbody>
                            ${tableRows.length > 0 ? tableRows : '<tr><td colspan="5">결재 대기중인 항목이 없습니다.</td></tr>'}
                        </tbody>
                    </table>
                </div>
            `;

			console.log("최종 HTML 생성 완료");
			mainContentArea.innerHTML = finalHTML;
		})
		.catch(err => {
			console.error("에러 발생:", err);
			alert('데이터 조회 중 오류가 발생했습니다: ' + err.message);

			mainContentArea.innerHTML = `
                <div class="approval-section">
                    <h2>결재 대기 목록</h2>
                    <div style="color: red; padding: 20px; border: 1px solid red; border-radius: 5px;">
                        데이터 로딩 실패: ${err.message}
                    </div>
                </div>
            `;
		});
}

function viewNoticeDetail(p_id) {
	fetch('/notice/getPendingById?p_id=' + p_id)
		.then(response => {
			if (!response.ok) {
				throw new Error('Network response was not ok');
			}
			return response.json();
		})
		.then(notice => {
			console.log('받은 데이터:', notice);

			// 이미 처리된 문서인지 확인
			const isProcessed = notice.status === '승인' || notice.status === '반려';

			mainContentArea.innerHTML = `
                    <div class="approval-section">
                        <h2>공지사항 결재</h2>
                        <div class="status-bar">
                            <span class="status status-${notice.status}">${notice.status}</span>
                        </div>
                        <div class="form-group">
                            <h3>제목</h3>
                            <input type="text" value="${notice.b_title || ''}" readonly>
                        </div>
                        <div class="form-group">
                            <h3>내용</h3>
                            <textarea readonly>${notice.b_content || ''}</textarea>
                        </div>
                        <div class="form-group">
                            <h3>작성자 의견</h3>
                            <input type="text" value="${notice.admin_comment || ''}" readonly>
                        </div>
                        ${notice.rejected_comment ? `
                            <div class="form-group">
                                <h3>반려 사유</h3>
                                <input type="text" value="${notice.rejected_comment}" readonly>
                            </div>
                        ` : ''}
                        <hr>
                        ${!isProcessed ? `
                            <div class="comment-section">
                                <h3>반려 사유</h3>
                                <textarea id="rejection-reason" placeholder="반려 시 사유를 입력합니다."></textarea>
                            </div>
                            <div class="actions">
                                <button id="approve-btn" data-pid="${p_id}">승인</button>
                                <button id="reject-btn" data-pid="${p_id}">반려</button>
                            </div>
                        ` : `
                            <div class="actions">
                                <p>이미 처리된 문서입니다.</p>
                            </div>
                        `}
                    </div>
                `;

			// 처리되지 않은 문서에만 버튼 이벤트 등록
			if (!isProcessed) {
				document.getElementById('approve-btn').addEventListener('click', (e) => {
					if (confirm('승인하시겠습니까?')) {
						handleDecision(e.target.dataset.pid, '승인');
					}
				});

				document.getElementById('reject-btn').addEventListener('click', (e) => {
					const reason = document.getElementById('rejection-reason').value;
					if (!reason.trim()) {
						alert('반려 사유를 입력해야 합니다.');
						return;
					}
					if (confirm('반려하시겠습니까?')) {
						handleDecision(e.target.dataset.pid, '반려', reason);
					}
				});
			}
		})
		.catch(err => {
			console.error('Error:', err);
			alert('데이터 조회 실패: ' + err.message);
		});
}

// 처리된 문서 목록 (승인, 반려만 표시)
function viewCompleteList() {
	console.log("처리된 문서 목록 조회 시작");

	fetch('/notice/getPendingList')
		.then(response => {
			if (!response.ok) {
				throw new Error(`HTTP error! status: ${response.status}`);
			}
			return response.json();
		})
		.then(data => {
			console.log("처리된 문서 조회 - 원본 데이터:", data);

			// 승인 또는 반려된 문서만 필터링 (결재대기 제외)
			const completedNotices = data.filter(item => {
				const status = item.status ? item.status.toString().trim() : '';
				const isCompleted = status === '승인' || status === '반려';
				console.log(`ID ${item.p_id}: 상태="${status}" -> 처리완료 여부: ${isCompleted}`);
				return isCompleted;
			});

			console.log("필터링된 처리 완료 문서:", completedNotices);

			let tableRows = completedNotices.map(notice => {
				const status = notice.status ? notice.status.toString().trim() : '상태없음';
				return `
                    <tr>
                        <td>${notice.p_id || '-'}</td>
                        <td>${notice.b_title || '-'}</td>
                        <td>${notice.b_created_at || '-'}</td>
                        <td>
                            <span class="status status-${status}">${status}</span>
                        </td>
                        <td>${notice.rejected_comment || '-'}</td>
                        <td>
                            <button onclick="viewNoticeDetail(${notice.p_id})">상세 보기</button>
                        </td>
                    </tr>
                `;
			}).join('');

			mainContentArea.innerHTML = `
                <div class="approval-section">
                    <h2>처리된 문서 목록</h2>
                    <div style="margin-bottom: 10px;">
                        <small>총 ${completedNotices.length}개의 처리된 문서 (승인/반려)</small>
                    </div>
                    <table class="notice-list-table">
                        <thead>
                            <tr>
                                <th>결재 ID</th>
                                <th>제목</th>
                                <th>작성일</th>
                                <th>최종 상태</th>
                                <th>반려 사유</th>
                                <th>작업</th>
                            </tr>
                        </thead>
                        <tbody>
                            ${tableRows.length ? tableRows : '<tr><td colspan="6">처리된 문서가 없습니다.</td></tr>'}
                        </tbody>
                    </table>
                </div>
            `;
		})
		.catch(err => {
			console.error("처리된 문서 조회 에러:", err);
			alert('처리된 문서 조회 중 오류가 발생했습니다: ' + err.message);
		});
}

//공지사항 전체 조회(처리된 문서)
function showAllNoticeList() {
	console.log("전체 목록 조회 시작");

	fetch('/notice/getPendingList')
		.then(response => {
			if (!response.ok) {
				throw new Error(`HTTP error! status: ${response.status}`);
			}
			return response.json();
		})
		.then(data => {
			console.log("전체 데이터:", data);

			// 상태별 개수 계산
			const statusCounts = data.reduce((counts, item) => {
				const status = item.status ? item.status.toString().trim() : '상태없음';
				counts[status] = (counts[status] || 0) + 1;
				return counts;
			}, {});

			console.log("상태별 개수:", statusCounts);

			let tableRows = data.map(notice => {
				const status = notice.status ? notice.status.toString().trim() : '상태없음';
				return `
                    <tr>
                        <td>${notice.p_id || '-'}</td>
                        <td>${notice.b_title || '-'}</td>
                        <td>${notice.b_created_at || '-'}</td>
                        <td>
                            <span class="status status-${status}">${status}</span>
                        </td>
                        <td>${notice.rejected_comment || '-'}</td>
                        <td>
                            <button onclick="viewNoticeDetail(${notice.p_id})">보기</button>
                        </td>
                    </tr>
                `;
			}).join('');

			const statusSummary = Object.entries(statusCounts)
				.map(([status, count]) => `${status}: ${count}개`)
				.join(', ');

			mainContentArea.innerHTML = `
                <div class="approval-section">
                    <h2>전체 문서 목록</h2>
                    <div style="margin-bottom: 10px;">
                        <small>총 ${data.length}개 항목 (${statusSummary})</small>
                    </div>
                    <table class="notice-list-table">
                        <thead>
                            <tr>
                                <th>결재 ID</th>
                                <th>제목</th>
                                <th>작성일</th>
                                <th>상태</th>
                                <th>반려 사유</th>
                                <th>작업</th>
                            </tr>
                        </thead>
                        <tbody>
                            ${tableRows.length ? tableRows : '<tr><td colspan="6">데이터가 없습니다.</td></tr>'}
                        </tbody>
                    </table>
                </div>
            `;
		})
		.catch(err => {
			console.error("전체 목록 조회 에러:", err);
			alert('데이터 조회 중 오류가 발생했습니다: ' + err.message);
		});
}
//미구현된 페이지에 접속 시 디폴트 값
function showPlaceholderContent(menuKey) {
	const menuName = topMenu.querySelector(`[data-menu="${menuKey}"]`).textContent;
	mainContentArea.innerHTML = `<h1>${menuName}</h1><p>이 페이지의 콘텐츠는 아직 구현되지 않았습니다.</p>`;
}
//상위 메뉴에 따라 사이드 바 메뉴 업데이트
function updateSidebar(menuKey) {
	const menuName = topMenu.querySelector(`[data-menu="${menuKey}"]`).textContent;
	document.getElementById('sidebar-title').textContent = menuName;
	sidebarMenu.innerHTML = '';
	(submenus[menuKey] || []).forEach(item => {
		const a = document.createElement('a');
		a.href = '#';
		a.dataset.action = item;
		a.textContent = item;
		sidebarMenu.appendChild(document.createElement('li')).appendChild(a);
	});
}
//공지사항 등록 및 결재상신 Function
function userApprovalFunction() {
	const title = document.querySelector('input[name=title]').value;
	const content = document.querySelector('textarea[name=content]').value;
	const admin_comment = document.querySelector('input[name=admin_comment]').value;

	// 입력값 검증
	if (!title.trim()) {
		alert('제목을 입력해주세요.');
		return;
	}
	if (!content.trim()) {
		alert('내용을 입력해주세요.');
		return;
	}

	const createdAt = new Date().toISOString().split('T')[0];
	const noticeData = {
		b_title: title,
		b_content: content,
		b_created_at: createdAt,
		status: '결재대기',
		admin_comment: admin_comment,
	};

	console.log("서버로 전송할 데이터:", noticeData);

	fetch('/notice/writeApproval', {
		method: 'POST',
		headers: {
			'Content-Type': 'application/json'
		},
		body: JSON.stringify(noticeData)
	})
		.then(response => {
			if (!response.ok) {
				throw new Error('사용자 권한 부적합!!');
			}
			return response.text();
		})
		.then(data => {
			console.log('서버 응답:', data);
			if (data === '데이터저장 성공') {
				alert('결재 상신 완료!');
				showPendingNoticeList();
			} else {
				alert('데이터 저장 실패: ' + data);
			}
		})
		.catch(err => {
			console.error('Error:', err);
			alert('Error: ' + err.message);
		});
}

// 결재 처리 함수 (승인/반려)
async function handleDecision(p_id, status, rejected_comment = '') {
	console.log(`서버로 결재 결정 전송: ID=${p_id}, 결정=${status}, 사유=${rejected_comment}`);

	const noticeData = {
		p_id: parseInt(p_id),
		status: status,
		rejected_comment: rejected_comment
	};

	try {
		const response = await fetch('/notice/uploadApprove', {
			method: 'POST',
			headers: {
				'Content-Type': 'application/json'
			},
			body: JSON.stringify(noticeData)
		});

		if (!response.ok) {
			throw new Error('결재권한 확인 필요!!');
		}

		const result = await response.text();
		console.log('서버 응답:', result);
		alert(result);

		// 결재 대기 목록 새로고침
		showPendingNoticeList();

	} catch (err) {
		console.error('Error:', err);
		alert('Error: ' + err.message);
	}
}

// 메인메뉴 버튼을 누르면 활성화 되도록 만들어주는 함수
topMenu.addEventListener('click', e => {
	if (e.target.tagName !== 'A') return;
	e.preventDefault();
	const menuKey = e.target.dataset.menu;
	updateSidebar(menuKey);
	if (menuKey === 'notice') showNoticeList();
	else if(menuKey === 'approval')showPendingNoticeList();
	else if (menuKey === 'logout') logout();
	else showPlaceholderContent(menuKey);
});

// 사이드바 버튼을 누르면 활성화 되도록 만들어주는 함수
sidebarMenu.addEventListener('click', e => {
	if (e.target.tagName !== 'A') return;
	e.preventDefault();
	const action = e.target.dataset.action;
	if (action === '결재 대기 목록') showPendingNoticeList();
	else if (action === '공지사항 등록') showUserNoticeForm();
	else if (action === '처리된 문서') viewCompleteList();
	else if (action === '공지사항 목록') showNoticeList();
	else showPlaceholderContent(action);
});

document.addEventListener('DOMContentLoaded', () => {
	topMenu.querySelector('[data-menu="notice"]').click();
});