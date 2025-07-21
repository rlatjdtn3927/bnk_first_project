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
	'approval': ['결재 문서 조회', '결재 대기 목록'],
	'notice': ['공지사항 목록', '공지사항 등록'],
	'product': ['상품 목록', '상품 등록'],
	'report': ['일일 보고서', '주간 보고서', '월간 보고서'],
	'management': ['사용자 목록', '사용자 등록', '사용자 권한 변경']
};

//공지사항 조회 - 순차 번호로 수정
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

			// 🔥 순차 번호 적용 (index + 1)
			let tableRows = data.map((notice, index) => {
				const sequentialNumber = index + 1; // 1부터 시작하는 순차 번호
				return `
                    <tr>
                        <td>${sequentialNumber}</td>
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
                        <div id="title-content" class="editable-content" 
                             data-field="b_title" 
                             data-notice-id="${notice.b_id}"
                             style="padding: 10px; border: 1px solid #ddd; border-radius: 5px;
                             background-color: #f8f9fa; font-weight: bold; cursor: pointer;
                             transition: background-color 0.2s;">
                            ${notice.b_title || '제목 없음'}
                        </div>
                        <small style="color: #666;">더블클릭하여 수정</small>
                    </div>

                    <div class="form-group">
                        <h3>내용</h3>
                        <div id="content-content" class="editable-content" 
                             data-field="b_content" 
                             data-notice-id="${notice.b_id}"
                             style="padding: 8px; border: 1px solid #ddd; border-radius: 5px;
                             background-color: #f8f9fa; min-height: 200px; cursor: pointer;
                             transition: background-color 0.2s;">
                            ${notice.b_content || '내용이 없습니다.'}
                        </div>
                        <small style="color: #666;">더블클릭하여 수정</small>
                    </div>

                    <!-- 관리자 의견 섹션 -->
                    <div class="form-group" style="margin-top: 30px; padding-top: 20px; border-top: 2px solid #e9ecef;">
                        <h3 style="color: #007bff;">💬 관리자 의견</h3>
                        <textarea id="admin-comment" 
                                  placeholder="수정 사유나 의견을 입력해주세요..."
                                  style="width: 100%; min-height: 80px; padding: 10px; 
                                  border: 1px solid #ddd; border-radius: 5px; 
                                  font-family: inherit; font-size: 14px; resize: vertical; 
                                  box-sizing: border-box; background-color: #fff;">
                        </textarea>
                        <small style="color: #666;">
                            수정 중인 내용이 있으면 저장 버튼으로 한번에 저장됩니다.
                        </small>
                    </div>

                    <!-- 통합 저장 버튼 영역 (항상 표시) -->
                    <div id="save-button-area" style="margin-top: 20px; text-align: center;">
                        <div id="edit-status" style="padding: 15px; background: #e3f2fd; border: 1px solid #2196f3; 
                             border-radius: 8px; margin-bottom: 15px; display: none;">
                            <p style="margin: 0; color: #1976d2; font-weight: bold;">
                                📝 수정 중인 항목이 있습니다
                            </p>
                            <small style="color: #666;">수정사항을 저장하거나 취소할 수 있습니다.</small>
                        </div>
                        
                        <button id="unified-save-btn" 
                                style="padding: 12px 30px; background: #28a745; color: white;
                                border: none; border-radius: 6px; cursor: pointer; font-size: 16px;
                                font-weight: bold; margin-right: 10px; box-shadow: 0 2px 5px rgba(0,0,0,0.2);
                                opacity: 0.5;" disabled>
                            💾 변경사항 저장
                        </button>
                        
                        <button id="unified-cancel-btn" 
                                style="padding: 12px 30px; background: #6c757d; color: white;
                                border: none; border-radius: 6px; cursor: pointer; font-size: 16px;
                                font-weight: bold; box-shadow: 0 2px 5px rgba(0,0,0,0.2);
                                opacity: 0.5;" disabled>
                            ❌ 수정 취소
                        </button>
                    </div>

                    <div style="display: flex; justify-content: space-between; align-items: center;
                         padding: 10px; background-color: #e9ecef; border-radius: 5px; margin-top: 20px;">
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

            // 인라인 수정 이벤트 리스너 추가
            initializeInlineEdit();
            // 통합 버튼 이벤트 리스너 추가
            initializeUnifiedButtons();
        })
        .catch(err => {
            console.error('공지사항 상세 조회 에러:', err);
            alert('공지사항 상세 조회 실패: ' + err.message);
        });
}

// 전역 변수로 수정 상태 관리
let editingElements = new Map(); // field -> {element, inputElement, originalText}
let currentNoticeId = null;

// 인라인 수정 기능 초기화
function initializeInlineEdit() {
    const editableElements = document.querySelectorAll('.editable-content');

    editableElements.forEach(element => {
        // 호버 효과
        element.addEventListener('mouseenter', function() {
            if (!this.classList.contains('editing')) {
                this.style.backgroundColor = '#e9ecef';
            }
        });

        element.addEventListener('mouseleave', function() {
            if (!this.classList.contains('editing')) {
                this.style.backgroundColor = '#f8f9fa';
            }
        });

        // 더블클릭 이벤트
        element.addEventListener('dblclick', function() {
            startInlineEdit(this);
        });
    });
}

// 통합 버튼 이벤트 리스너
function initializeUnifiedButtons() {
    const saveBtn = document.getElementById('unified-save-btn');
    const cancelBtn = document.getElementById('unified-cancel-btn');

    saveBtn.addEventListener('click', saveAllChanges);
    cancelBtn.addEventListener('click', cancelAllChanges);
}

// 저장 버튼 영역 표시/숨김
function toggleSaveButtonArea() {
    const editStatus = document.getElementById('edit-status');
    const saveBtn = document.getElementById('unified-save-btn');
    const cancelBtn = document.getElementById('unified-cancel-btn');
    
    if (editingElements.size > 0) {
        // 수정 중일 때
        editStatus.style.display = 'block';
        saveBtn.disabled = false;
        cancelBtn.disabled = false; 
        saveBtn.style.opacity = '1';
        cancelBtn.style.opacity = '1';
        saveBtn.style.cursor = 'pointer';
        cancelBtn.style.cursor = 'pointer';
    } else {
        // 수정 중이 아닐 때
        editStatus.style.display = 'none';
        saveBtn.disabled = true;
        cancelBtn.disabled = true;
        saveBtn.style.opacity = '0.5';
        cancelBtn.style.opacity = '0.5';
        saveBtn.style.cursor = 'not-allowed';
        cancelBtn.style.cursor = 'not-allowed';
    }
}

// 관리자 의견 가져오기 함수
function getAdminComment() {
    const adminCommentElement = document.getElementById('admin-comment');
    return adminCommentElement ? adminCommentElement.value.trim() : '';
}

// 인라인 수정 시작
function startInlineEdit(element) {
    if (element.classList.contains('editing')) return;

    element.classList.add('editing');
    const originalText = element.textContent;
    const field = element.getAttribute('data-field');
    const noticeId = element.getAttribute('data-notice-id');
    currentNoticeId = noticeId;

    // 기존 내용을 저장
    element.setAttribute('data-original', originalText);

    // 입력 필드 생성
    let inputElement;
    if (field === 'b_content') {
        inputElement = document.createElement('textarea');
        inputElement.style.minHeight = '200px';
        inputElement.style.resize = 'vertical';
    } else {
        inputElement = document.createElement('input');
        inputElement.type = 'text';
    }

    inputElement.value = originalText;
    inputElement.style.width = '100%';
    inputElement.style.padding = '8px';
    inputElement.style.border = '2px solid #007bff';
    inputElement.style.borderRadius = '5px';
    inputElement.style.fontFamily = 'inherit';
    inputElement.style.fontSize = 'inherit';
    inputElement.style.backgroundColor = 'white';
    inputElement.style.boxSizing = 'border-box';

    // 수정 중인 요소들을 맵에 저장
    editingElements.set(field, {
        element: element,
        inputElement: inputElement,
        originalText: originalText
    });

    // 기존 내용 숨기기
    element.style.display = 'none';

    // 입력 필드 추가
    element.parentNode.appendChild(inputElement);

    // 포커스 설정
    inputElement.focus();
    inputElement.select();

    // 저장 버튼 영역 활성화
    toggleSaveButtonArea();

    // Escape 키로 개별 취소
    inputElement.addEventListener('keydown', function(e) {
        if (e.key === 'Escape') {
            cancelSingleEdit(field);
        }
    });

    // Enter로 전체 저장 (제목인 경우)
    if (field === 'b_title') {
        inputElement.addEventListener('keypress', function(e) {
            if (e.key === 'Enter') {
                saveAllChanges();
            }
        });
    }

    // Ctrl+Enter로 전체 저장
    inputElement.addEventListener('keydown', function(e) {
        if (e.ctrlKey && e.key === 'Enter') {
            saveAllChanges();
        }
    });
}

// 개별 수정 취소
function cancelSingleEdit(field) {
    const editData = editingElements.get(field);
    if (!editData) return;

    const { element, inputElement } = editData;
    
    element.classList.remove('editing');
    element.style.display = 'block';
    element.style.backgroundColor = '#f8f9fa';
    
    inputElement.remove();
    editingElements.delete(field);
    
    toggleSaveButtonArea();
}

// 모든 변경사항 저장
function saveAllChanges() {
    if (editingElements.size === 0) {
        alert('수정된 내용이 없습니다.');
        return;
    }

    // 입력값 검증
    for (let [field, editData] of editingElements) {
        const newValue = editData.inputElement.value.trim();
        if (!newValue) {
            alert(`${field === 'b_title' ? '제목' : '내용'}을 입력해주세요.`);
            editData.inputElement.focus();
            return;
        }
    }

    // 로딩 상태로 변경
    const saveBtn = document.getElementById('unified-save-btn');
    const cancelBtn = document.getElementById('unified-cancel-btn');
    
    saveBtn.disabled = true;
    cancelBtn.disabled = true;
    saveBtn.textContent = '⏳ 저장 중...';
    saveBtn.style.backgroundColor = '#6c757d';
    
    // 모든 입력 필드 비활성화
    editingElements.forEach(editData => {
        editData.inputElement.disabled = true;
    });

    const adminComment = getAdminComment();
    const updates = [];
    
    // 각 수정사항을 배열에 저장
    editingElements.forEach((editData, field) => {
        const newValue = editData.inputElement.value.trim();
        updates.push({
            field: field,
            value: newValue,
            editData: editData
        });
    });

    // 순차적으로 업데이트 실행
    processUpdates(updates, adminComment, 0);
}

// 순차적으로 업데이트 처리
function processUpdates(updates, adminComment, index) {
    if (index >= updates.length) {
        // 모든 업데이트 완료
        handleAllUpdatesComplete();
        return;
    }

    const update = updates[index];
    
    updateNoticeField(currentNoticeId, update.field, update.value, adminComment)
        .then(success => {
            if (success) {
                // 성공 시 화면 업데이트
                update.editData.element.textContent = update.value;
                cleanupSingleEdit(update.field);
                
                // 다음 업데이트 처리
                processUpdates(updates, adminComment, index + 1);
            } else {
                throw new Error('서버에서 실패 응답');
            }
        })
        .catch(error => {
            console.error('수정 실패:', error);
            handleUpdateError(error.message);
        });
}

// 모든 업데이트 완료 처리
function handleAllUpdatesComplete() {
    // 관리자 의견 초기화
    const adminCommentElement = document.getElementById('admin-comment');
    if (adminCommentElement) {
        adminCommentElement.value = '';
    }

    // 저장 버튼 영역 비활성화
    toggleSaveButtonArea();
    
    // 성공 메시지
    showSuccessMessage('모든 변경사항이 저장되었습니다.');
    
    // 버튼 상태 복원
    resetButtonState();
}

// 업데이트 에러 처리
function handleUpdateError(errorMessage) {
    alert('수정 중 오류가 발생했습니다: ' + errorMessage);
    
    // 모든 입력 필드 다시 활성화
    editingElements.forEach(editData => {
        editData.inputElement.disabled = false;
    });
    
    // 버튼 상태 복원
    resetButtonState();
}

// 버튼 상태 복원
function resetButtonState() {
    const saveBtn = document.getElementById('unified-save-btn');
    const cancelBtn = document.getElementById('unified-cancel-btn');
    
    saveBtn.disabled = false;
    cancelBtn.disabled = false;
    saveBtn.textContent = '💾 변경사항 저장';
    saveBtn.style.backgroundColor = '#28a745';
}

// 단일 수정 정리
function cleanupSingleEdit(field) {
    const editData = editingElements.get(field);
    if (!editData) return;

    const { element, inputElement } = editData;
    
    element.classList.remove('editing');
    element.style.display = 'block';
    element.style.backgroundColor = '#f8f9fa';
    
    inputElement.remove();
    editingElements.delete(field);
}

// 모든 수정 취소
function cancelAllChanges() {
    if (editingElements.size === 0) return;

    if (confirm('모든 수정 내용을 취소하시겠습니까? 입력한 내용이 모두 사라집니다.')) {
        // 모든 수정사항 원복
        editingElements.forEach((editData, field) => {
            cleanupSingleEdit(field);
        });
        
        editingElements.clear();
        toggleSaveButtonArea();
    }
}

// 서버에 공지사항 필드 업데이트 요청
function updateNoticeField(noticeId, field, value, adminComment) {
    return fetch('/notice/updateField', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify({
            b_id: noticeId,
            field: field,
            value: value,
            admin_comment: adminComment || ''
        })
    })
        .then(response => {
            if (!response.ok) {
                throw new Error(`HTTP error! status: ${response.status}`);
            }
            return response.json();
        })
        .then(data => {
            return data.success === true;
        });
}

// 성공 메시지 표시
function showSuccessMessage(message) {
    const successDiv = document.createElement('div');
    successDiv.textContent = message;
    successDiv.style.position = 'fixed';
    successDiv.style.top = '20px';
    successDiv.style.right = '20px';
    successDiv.style.padding = '15px 25px';
    successDiv.style.backgroundColor = '#28a745';
    successDiv.style.color = 'white';
    successDiv.style.borderRadius = '8px';
    successDiv.style.zIndex = '9999';
    successDiv.style.boxShadow = '0 4px 15px rgba(0,0,0,0.2)';
    successDiv.style.fontWeight = 'bold';
    successDiv.style.animation = 'slideIn 0.3s ease-out';

    // 애니메이션 CSS 추가
    if (!document.getElementById('success-animation-style')) {
        const style = document.createElement('style');
        style.id = 'success-animation-style';
        style.textContent = `
            @keyframes slideIn {
                from { transform: translateX(100%); opacity: 0; }
                to { transform: translateX(0); opacity: 1; }
            }
        `;
        document.head.appendChild(style);
    }

    document.body.appendChild(successDiv);

    setTimeout(() => {
        successDiv.style.animation = 'slideIn 0.3s ease-out reverse';
        setTimeout(() => successDiv.remove(), 300);
    }, 3000);
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

			// 결재대기와 수정요청 상태인 항목만 필터링
			const pendingOnlyNotices = data.filter(item => {
			    const status = item.status ? item.status.toString().trim() : '';
			    const isPending = status === '결재대기';
			    const isModificationRequest = status === '수정요청';
			    const shouldShow = isPending || isModificationRequest;
			    
			    console.log(`ID ${item.p_id}: 상태="${status}" -> 표시 여부: ${shouldShow} (결재대기: ${isPending}, 수정요청: ${isModificationRequest})`);
			    return shouldShow;
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
	else if (menuKey === 'approval') showPendingNoticeList();
	else if (menuKey === 'management') showUserList();
	else if (menuKey === 'logout') logout();
	else if(menuKey === 'product') showCommodityList();
	else showPlaceholderContent(menuKey);
});

// 사이드바 버튼을 누르면 활성화 되도록 만들어주는 함수
sidebarMenu.addEventListener('click', e => {
	if (e.target.tagName !== 'A') return;
	e.preventDefault();
	const action = e.target.dataset.action;
	if (action === '결재 대기 목록') showPendingNoticeList();
	else if (action === '공지사항 등록') showUserNoticeForm();
	else if (action === '결재 문서 조회') viewCompleteList();
	else if (action === '공지사항 목록') showNoticeList();
	else if (action === '사용자 목록') showUserList();
	else if (action === '사용자 등록') showUserRegistrationForm();
	else if (action === '사용자 권한 변경') showUserRoleManagement();
	else showPlaceholderContent(action);
});

document.addEventListener('DOMContentLoaded', () => {
	topMenu.querySelector('[data-menu="notice"]').click();
});

//----------------------------------------------------------------
//------------------사용자 관리 영역----------------------------------

// 사용자 목록 조회
function showUserList() {
	console.log("사용자 목록 조회 시작");

	fetch('/member/getUserList')
		.then(response => {
			console.log("사용자 목록 응답 상태:", response.status);
			if (!response.ok) {
				throw new Error(`HTTP error! status: ${response.status}`);
			}
			return response.json();
		})
		.then(data => {
			console.log("사용자 목록 데이터:", data);

			if (!Array.isArray(data)) {
				console.error("받은 데이터가 배열이 아닙니다:", typeof data);
				throw new Error("서버에서 잘못된 형식의 데이터를 받았습니다.");
			}

			console.log("사용자 개수:", data.length);

			let tableRows = data.map(user => {
				// 역할 클래스 결정 (user, master, admin)
				const roleClass = user.roll === 'admin' ? 'status-admin' :
					user.roll === 'master' ? 'status-master' : 'status-user';

				return `
                    <tr>
                        <td>${user.userid || '-'}</td>
                        <td>${user.username || '-'}</td>
                        <td>${user.name || '-'}</td>
                        <td>${user.email || '-'}</td>
                        <td>${user.phone || '-'}</td>
                        <td>
                            <span class="status ${roleClass}">${user.roll || '역할없음'}</span>
                        </td>
                        <td>
                            <button onclick="viewUserDetail(${user.userid})" 
                                    style="padding: 5px 10px; border: 1px solid #007bff; 
                                           background: #007bff; color: white; border-radius: 3px; cursor: pointer; margin-right: 5px;">
                                상세
                            </button>
                            <button onclick="editUser(${user.userid})" 
                                    style="padding: 5px 10px; border: 1px solid #28a745; 
                                           background: #28a745; color: white; border-radius: 3px; cursor: pointer;">
                                수정
                            </button>
                        </td>
                    </tr>
                `;
			}).join('');

			const finalHTML = `
                <div class="approval-section">
                    <h2>사용자 목록</h2>
                    <div style="margin-bottom: 15px; display: flex; justify-content: space-between; align-items: center;">
                        <small style="color: #666;">총 ${data.length}명의 사용자</small>
                        <button onclick="showUserRegistrationForm()" 
                                style="padding: 8px 15px; background: #28a745; color: white; 
                                       border: none; border-radius: 5px; cursor: pointer;">
                            새 사용자 등록
                        </button>
                    </div>
                    
                    <style>
                        .status-user { 
                            background-color: #e3f2fd; 
                            color: #1976d2; 
                            padding: 4px 8px; 
                            border-radius: 4px; 
                            font-size: 12px; 
                            font-weight: bold;
                        }
                        .status-master { 
                            background-color: #fff3e0; 
                            color: #f57c00; 
                            padding: 4px 8px; 
                            border-radius: 4px; 
                            font-size: 12px; 
                            font-weight: bold;
                        }
                        .status-admin { 
                            background-color: #ffebee; 
                            color: #d32f2f; 
                            padding: 4px 8px; 
                            border-radius: 4px; 
                            font-size: 12px; 
                            font-weight: bold;
                        }
                    </style>
                    
                    <table class="notice-list-table">
                        <thead>
                            <tr>
                                <th style="width: 80px;">회원ID</th>
                                <th>유저 아이디</th>
                                <th>이름</th>
                                <th>이메일</th>
                                <th>전화번호</th>
                                <th style="width: 100px;">역할</th>
                                <th style="width: 150px;">작업</th>
                            </tr>
                        </thead>
                        <tbody>
                            ${tableRows.length > 0 ? tableRows : '<tr><td colspan="7">등록된 사용자가 없습니다.</td></tr>'}
                        </tbody>
                    </table>
                </div>
            `;

			console.log("사용자 목록 HTML 생성 완료");
			mainContentArea.innerHTML = finalHTML;
		})
		.catch(err => {
			console.error("사용자 목록 조회 에러:", err);
			alert('사용자 목록 조회 중 오류가 발생했습니다: ' + err.message);

			mainContentArea.innerHTML = `
                <div class="approval-section">
                    <h2>사용자 목록</h2>
                    <div style="color: red; padding: 20px; border: 1px solid red; border-radius: 5px;">
                        사용자 목록 로딩 실패: ${err.message}
                    </div>
                </div>
            `;
		});
}

// 사용자 상세 정보 조회
function viewUserDetail(userid) {
	console.log("사용자 상세 정보 조회 - ID:", userid);

	fetch(`/member/getUserDetail?userid=${userid}`)
		.then(response => {
			if (!response.ok) {
				throw new Error(`HTTP error! status: ${response.status}`);
			}
			return response.json();
		})
		.then(user => {
			console.log('사용자 상세 정보:', user);

			const roleClass = user.roll === 'admin' ? 'status-admin' :
				user.roll === 'master' ? 'status-master' : 'status-user';

			mainContentArea.innerHTML = `
                <div class="approval-section">
                    <div style="margin-bottom: 20px;">
                        <button onclick="showUserList()" 
                                style="padding: 8px 15px; background: #6c757d; color: white; 
                                       border: none; border-radius: 5px; cursor: pointer;">
                            ← 목록으로 돌아가기
                        </button>
                    </div>
                    
                    <h2>사용자 상세 정보</h2>
                    
                    <style>
                        .status-user { background-color: #e3f2fd; color: #1976d2; padding: 4px 8px; border-radius: 4px; font-size: 12px; font-weight: bold; }
                        .status-master { background-color: #fff3e0; color: #f57c00; padding: 4px 8px; border-radius: 4px; font-size: 12px; font-weight: bold; }
                        .status-admin { background-color: #ffebee; color: #d32f2f; padding: 4px 8px; border-radius: 4px; font-size: 12px; font-weight: bold; }
                    </style>
                    
                    <div class="form-group">
                        <h3>회원 ID</h3>
                        <div style="padding: 10px; border: 1px solid #ddd; border-radius: 5px; 
                                    background-color: #f8f9fa;">
                            ${user.userid || '-'}
                        </div>
                    </div>
                    
                    <div class="form-group">
                        <h3>유저 아이디</h3>
                        <div style="padding: 10px; border: 1px solid #ddd; border-radius: 5px; 
                                    background-color: #f8f9fa;">
                            ${user.username || '-'}
                        </div>
                    </div>
                    
                    <div class="form-group">
                        <h3>이름</h3>
                        <div style="padding: 10px; border: 1px solid #ddd; border-radius: 5px; 
                                    background-color: #f8f9fa;">
                            ${user.name || '-'}
                        </div>
                    </div>
                    
                    <div class="form-group">
                        <h3>이메일</h3>
                        <div style="padding: 10px; border: 1px solid #ddd; border-radius: 5px; 
                                    background-color: #f8f9fa;">
                            ${user.email || '-'}
                        </div>
                    </div>
                    
                    <div class="form-group">
                        <h3>전화번호</h3>
                        <div style="padding: 10px; border: 1px solid #ddd; border-radius: 5px; 
                                    background-color: #f8f9fa;">
                            ${user.phone || '-'}
                        </div>
                    </div>
                    
                    <div class="form-group">
                        <h3>역할</h3>
                        <div style="padding: 10px; border: 1px solid #ddd; border-radius: 5px; 
                                    background-color: #f8f9fa;">
                            <span class="status ${roleClass}">${user.roll || '역할없음'}</span>
                        </div>
                    </div>
                    
                    <div class="actions" style="margin-top: 20px;">
                        <button onclick="editUser(${user.userid})" 
                                style="padding: 10px 20px; background: #28a745; color: white; 
                                       border: none; border-radius: 5px; cursor: pointer; margin-right: 10px;">
                            수정
                        </button>
                        <button onclick="showUserList()" 
                                style="padding: 10px 20px; background: #007bff; color: white; 
                                       border: none; border-radius: 5px; cursor: pointer;">
                            목록으로
                        </button>
                    </div>
                </div>
            `;
		})
		.catch(err => {
			console.error('사용자 상세 조회 에러:', err);
			alert('사용자 상세 조회 실패: ' + err.message);
		});
}

// 사용자 등록 폼
function showUserRegistrationForm() {
	mainContentArea.innerHTML = `
        <div class="approval-section">
            <h2>새 사용자 등록</h2>
            <div class="form-group">
                <h3>유저 아이디</h3>
                <input type="text" id="username" placeholder="유저 아이디 입력 (로그인용)">
            </div>
            <div class="form-group">
                <h3>비밀번호</h3>
                <input type="password" id="password" placeholder="비밀번호 입력">
            </div>
            <div class="form-group">
                <h3>이름</h3>
                <input type="text" id="name" placeholder="실명 입력">
            </div>
            <div class="form-group">
                <h3>이메일</h3>
                <input type="email" id="email" placeholder="이메일 주소 입력">
            </div>
            <div class="form-group">
                <h3>전화번호</h3>
                <input type="tel" id="phone" placeholder="전화번호 입력 (예: 010-1234-5678)" maxlength="13">
            </div>
            <div class="form-group">
                <h3>역할</h3>
                <select id="roll">
                    <option value="">역할 선택</option>
                    <option value="user">사용자</option>
                    <option value="master">마스터</option>
                    <option value="admin">관리자</option>
                </select>
            </div>
            <div class="actions">
                <button onclick="registerUser()" 
                        style="padding: 10px 20px; background: #28a745; color: white; 
                               border: none; border-radius: 5px; cursor: pointer; margin-right: 10px;">
                    등록
                </button>
                <button onclick="showUserList()" 
                        style="padding: 10px 20px; background: #6c757d; color: white; 
                               border: none; border-radius: 5px; cursor: pointer;">
                    취소
                </button>
            </div>
        </div>
    `;

	// 전화번호 자동 포맷팅
	document.getElementById('phone').addEventListener('input', function(e) {
		let value = e.target.value.replace(/[^0-9]/g, '');
		if (value.length >= 3 && value.length <= 6) {
			value = value.slice(0, 3) + '-' + value.slice(3);
		} else if (value.length >= 7) {
			value = value.slice(0, 3) + '-' + value.slice(3, 7) + '-' + value.slice(7, 11);
		}
		e.target.value = value;
	});
}

// 사용자 등록 처리
function registerUser() {
	const username = document.getElementById('username').value;
	const password = document.getElementById('password').value;
	const name = document.getElementById('name').value;
	const email = document.getElementById('email').value;
	const phone = document.getElementById('phone').value;
	const roll = document.getElementById('roll').value;

	// 입력값 검증
	if (!username.trim()) {
		alert('유저 아이디를 입력해주세요.');
		return;
	}
	if (!password.trim()) {
		alert('비밀번호를 입력해주세요.');
		return;
	}
	if (!name.trim()) {
		alert('이름을 입력해주세요.');
		return;
	}
	if (!email.trim()) {
		alert('이메일을 입력해주세요.');
		return;
	}
	if (!phone.trim()) {
		alert('전화번호를 입력해주세요.');
		return;
	}
	if (!roll) {
		alert('역할을 선택해주세요.');
		return;
	}

	// 역할 유효성 검사
	if (!['user', 'master', 'admin'].includes(roll)) {
		alert('올바른 역할을 선택해주세요.');
		return;
	}

	// 이메일 형식 검증
	const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
	if (!emailRegex.test(email)) {
		alert('올바른 이메일 형식을 입력해주세요.');
		return;
	}

	// 전화번호 형식 검증
	const phoneRegex = /^\d{3}-\d{4}-\d{4}$/;
	if (!phoneRegex.test(phone)) {
		alert('올바른 전화번호 형식을 입력해주세요. (예: 010-1234-5678)');
		return;
	}

	const userData = {
		username: username,
		password: password,
		name: name,
		email: email,
		phone: phone,
		roll: roll
	};

	console.log("서버로 전송할 사용자 데이터:", userData);

	fetch('/member/registerUser', {
		method: 'POST',
		headers: {
			'Content-Type': 'application/json'
		},
		body: JSON.stringify(userData)
	})
		.then(response => {
			if (!response.ok) {
				throw new Error('사용자 등록 권한이 부족합니다.');
			}
			return response.text();
		})
		.then(data => {
			console.log('서버 응답:', data);
			if (data === '사용자 등록 성공') {
				alert('사용자 등록이 완료되었습니다.');
				showUserList();
			} else {
				alert('사용자 등록 실패: ' + data);
			}
		})
		.catch(err => {
			console.error('Error:', err);
			alert('Error: ' + err.message);
		});
}

// 사용자 수정 폼
function editUser(userid) {
	console.log("사용자 수정 폼 - ID:", userid);

	fetch(`/member/getUserDetail?userid=${userid}`)
		.then(response => {
			if (!response.ok) {
				throw new Error(`HTTP error! status: ${response.status}`);
			}
			return response.json();
		})
		.then(user => {
			mainContentArea.innerHTML = `
                <div class="approval-section">
                    <h2>사용자 정보 수정</h2>
                    <input type="hidden" id="edit-userid" value="${user.userid}">
                    
                    <div class="form-group">
                        <h3>회원 ID</h3>
                        <div style="padding: 10px; border: 1px solid #ddd; border-radius: 5px; 
                                    background-color: #f8f9fa; color: #666;">
                            ${user.userid} (수정 불가)
                        </div>
                    </div>
                    
                    <div class="form-group">
                        <h3>유저 아이디</h3>
                        <input type="text" id="edit-username" value="${user.username || ''}" placeholder="유저 아이디 입력">
                    </div>
                    
                    <div class="form-group">
                        <h3>비밀번호</h3>
                        <input type="password" id="edit-password" placeholder="새 비밀번호 입력 (변경 시에만)">
                        <small style="color: #666;">비밀번호를 변경하지 않으려면 비워두세요.</small>
                    </div>
                    
                    <div class="form-group">
                        <h3>이름</h3>
                        <input type="text" id="edit-name" value="${user.name || ''}" placeholder="이름 입력">
                    </div>
                    
                    <div class="form-group">
                        <h3>이메일</h3>
                        <input type="email" id="edit-email" value="${user.email || ''}" placeholder="이메일 주소 입력">
                    </div>
                    
                    <div class="form-group">
                        <h3>전화번호</h3>
                        <input type="tel" id="edit-phone" value="${user.phone || ''}" placeholder="전화번호 입력" maxlength="13">
                    </div>
                    
                    <div class="form-group">
                        <h3>역할</h3>
                        <select id="edit-roll">
                            <option value="">역할 선택</option>
                            <option value="user" ${user.roll === 'user' ? 'selected' : ''}>사용자</option>
                            <option value="master" ${user.roll === 'master' ? 'selected' : ''}>마스터</option>
                            <option value="admin" ${user.roll === 'admin' ? 'selected' : ''}>관리자</option>
                        </select>
                    </div>
                    
                    <div class="actions">
                        <button onclick="updateUser()" 
                                style="padding: 10px 20px; background: #28a745; color: white; 
                                       border: none; border-radius: 5px; cursor: pointer; margin-right: 10px;">
                            수정 완료
                        </button>
                        <button onclick="deleteUser(${user.userid})" 
                                style="padding: 10px 20px; background: #dc3545; color: white; 
                                       border: none; border-radius: 5px; cursor: pointer; margin-right: 10px;">
                            사용자 삭제
                        </button>
                        <button onclick="showUserList()" 
                                style="padding: 10px 20px; background: #6c757d; color: white; 
                                       border: none; border-radius: 5px; cursor: pointer;">
                            취소
                        </button>
                    </div>
                </div>
            `;

			// 전화번호 자동 포맷팅
			document.getElementById('edit-phone').addEventListener('input', function(e) {
				let value = e.target.value.replace(/[^0-9]/g, '');
				if (value.length >= 3 && value.length <= 6) {
					value = value.slice(0, 3) + '-' + value.slice(3);
				} else if (value.length >= 7) {
					value = value.slice(0, 3) + '-' + value.slice(3, 7) + '-' + value.slice(7, 11);
				}
				e.target.value = value;
			});
		})
		.catch(err => {
			console.error('사용자 정보 로드 에러:', err);
			alert('사용자 정보 로드 실패: ' + err.message);
		});
}

// 사용자 정보 업데이트 처리
function updateUser() {
	const userid = document.getElementById('edit-userid').value;
	const username = document.getElementById('edit-username').value;
	const password = document.getElementById('edit-password').value;
	const name = document.getElementById('edit-name').value;
	const email = document.getElementById('edit-email').value;
	const phone = document.getElementById('edit-phone').value;
	const roll = document.getElementById('edit-roll').value;

	// 입력값 검증
	if (!username.trim()) {
		alert('유저 아이디를 입력해주세요.');
		return;
	}
	if (!name.trim()) {
		alert('이름을 입력해주세요.');
		return;
	}
	if (!email.trim()) {
		alert('이메일을 입력해주세요.');
		return;
	}
	if (!phone.trim()) {
		alert('전화번호를 입력해주세요.');
		return;
	}
	if (!roll) {
		alert('역할을 선택해주세요.');
		return;
	}

	// 역할 유효성 검사
	if (!['user', 'master', 'admin'].includes(roll)) {
		alert('올바른 역할을 선택해주세요.');
		return;
	}

	// 이메일 형식 검증
	const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
	if (!emailRegex.test(email)) {
		alert('올바른 이메일 형식을 입력해주세요.');
		return;
	}

	// 전화번호 형식 검증
	const phoneRegex = /^\d{3}-\d{4}-\d{4}$/;
	if (!phoneRegex.test(phone)) {
		alert('올바른 전화번호 형식을 입력해주세요. (예: 010-1234-5678)');
		return;
	}

	const userData = {
		userid: parseInt(userid),
		username: username,
		name: name,
		email: email,
		phone: phone,
		roll: roll
	};

	// 비밀번호가 입력된 경우에만 포함
	if (password.trim()) {
		userData.password = password;
	}

	console.log("서버로 전송할 수정 데이터:", userData);

	fetch('/member/updateUser', {
		method: 'POST',
		headers: {
			'Content-Type': 'application/json'
		},
		body: JSON.stringify(userData)
	})
		.then(response => {
			if (!response.ok) {
				throw new Error('사용자 정보 수정 권한이 부족합니다.');
			}
			return response.text();
		})
		.then(data => {
			console.log('서버 응답:', data);
			if (data === '사용자 정보 수정 성공') {
				alert('사용자 정보가 수정되었습니다.');
				showUserList();
			} else {
				alert('사용자 정보 수정 실패: ' + data);
			}
		})
		.catch(err => {
			console.error('Error:', err);
			alert('Error: ' + err.message);
		});
}

// 사용자 삭제
function deleteUser(userid) {
	if (!confirm('정말로 이 사용자를 삭제하시겠습니까?\n삭제된 사용자 정보는 복구할 수 없습니다.')) {
		return;
	}

	fetch('/member/deleteUser', {
		method: 'POST',
		headers: {
			'Content-Type': 'application/json'
		},
		body: JSON.stringify({ userid: userid })
	})
		.then(response => {
			if (!response.ok) {
				throw new Error('사용자 삭제 권한이 부족합니다.');
			}
			return response.text();
		})
		.then(data => {
			console.log('서버 응답:', data);
			if (data === '사용자 삭제 성공') {
				alert('사용자가 삭제되었습니다.');
				showUserList();
			} else {
				alert('사용자 삭제 실패: ' + data);
			}
		})
		.catch(err => {
			console.error('Error:', err);
			alert('Error: ' + err.message);
		});
}

// 사용자 권한 변경
function showUserRoleManagement() {
	console.log("사용자 권한 변경 페이지");

	fetch('/member/getUserList')
		.then(response => {
			if (!response.ok) {
				throw new Error(`HTTP error! status: ${response.status}`);
			}
			return response.json();
		})
		.then(data => {
			let tableRows = data.map(user => {
				return `
                    <tr>
                        <td>${user.userid || '-'}</td>
                        <td>${user.username || '-'}</td>
                        <td>${user.name || '-'}</td>
                        <td>${user.email || '-'}</td>
                        <td>
                            <select id="role-${user.userid}">
                                <option value="user" ${user.roll === 'user' ? 'selected' : ''}>사용자</option>
                                <option value="master" ${user.roll === 'master' ? 'selected' : ''}>마스터</option>
                                <option value="admin" ${user.roll === 'admin' ? 'selected' : ''}>관리자</option>
                            </select>
                        </td>
                        <td>
                            <button onclick="applyRoleChange(${user.userid})" 
                                    style="padding: 5px 10px; background: #28a745; color: white; 
                                           border: none; border-radius: 3px; cursor: pointer;">
                                적용
                            </button>
                        </td>
                    </tr>
                `;
			}).join('');

			mainContentArea.innerHTML = `
                <div class="approval-section">
                    <h2>사용자 권한 변경</h2>
                    <div style="margin-bottom: 15px;">
                        <small style="color: #666;">사용자의 권한을 변경할 수 있습니다.</small>
                    </div>
                    <table class="notice-list-table">
                        <thead>
                            <tr>
                                <th>회원ID</th>
                                <th>유저 아이디</th>
                                <th>이름</th>
                                <th>이메일</th>
                                <th>역할</th>
                                <th>작업</th>
                            </tr>
                        </thead>
                        <tbody>
                            ${tableRows.length > 0 ? tableRows : '<tr><td colspan="6">사용자가 없습니다.</td></tr>'}
                        </tbody>
                    </table>
                </div>
            `;
		})
		.catch(err => {
			console.error('사용자 권한 변경 페이지 로드 에러:', err);
			alert('사용자 권한 변경 페이지 로드 실패: ' + err.message);
		});
}

// 권한 변경 적용
function applyRoleChange(userid) {
	const newRole = document.getElementById(`role-${userid}`).value;

	if (confirm(`사용자 권한을 '${newRole}'로 변경하시겠습니까?`)) {
		const userData = {
			userid: userid,
			roll: newRole
		};

		fetch('/member/changeUserRole', {
			method: 'POST',
			headers: {
				'Content-Type': 'application/json'
			},
			body: JSON.stringify(userData)
		})
			.then(response => {
				if (!response.ok) {
					throw new Error('권한 변경 권한이 부족합니다.');
				}
				return response.text();
			})
			.then(data => {
				console.log('권한 변경 결과:', data);
				if (data === '권한 변경 성공') {
					alert('사용자 권한이 변경되었습니다.');
					showUserRoleManagement(); // 페이지 새로고침
				} else {
					alert('권한 변경 실패: ' + data);
				}
			})
			.catch(err => {
				console.error('Error:', err);
				alert('Error: ' + err.message);
			});
	}
}

/**************상품 관련 함수****************/

function showCommodityList() {
	mainContentArea.innerHTML;

}
