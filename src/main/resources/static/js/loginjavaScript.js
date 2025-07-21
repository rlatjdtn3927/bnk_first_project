/**
 * 로그인 자바 스크립트 파일
 */
// 전역 변수
let currentTab = 'normal';

// DOM 요소들
const tabButtons = document.querySelectorAll('.tab-button');
const loginForms = document.querySelectorAll('.login-form');
const normalLoginForm = document.getElementById('normal-login-form');
const certificateLoginDiv = document.getElementById('certificate-login');
const normalErrorMessage = document.getElementById('normal-error-message');
const certificateErrorMessage = document.getElementById('certificate-error-message');
const certificateStatus = document.getElementById('certificate-status');
const certificateBtn = document.getElementById('certificate-btn');
const certificateFile = document.getElementById('certificate-file');
const selectedFileInfo = document.getElementById('selected-file-info');
const fileName = document.getElementById('file-name');
const fileSize = document.getElementById('file-size');
const removeFileBtn = document.getElementById('remove-file-btn');
const certificatePasswordSection = document.getElementById('certificate-password-section');
const certificatePassword = document.getElementById('certificate-password');
const certificateLoginBtn = document.getElementById('certificate-login-btn');

// 탭 전환 함수
function switchTab(tab) {
	currentTab = tab;

	// 탭 버튼 활성화 상태 변경
	tabButtons.forEach(btn => {
		btn.classList.remove('active');
		if (btn.dataset.tab === tab) {
			btn.classList.add('active');
		}
	});

	// 로그인 폼 표시/숨김
	loginForms.forEach(form => {
		form.classList.remove('active');
	});

	if (tab === 'normal') {
		normalLoginForm.classList.add('active');
		clearMessages();
	} else if (tab === 'certificate') {
		certificateLoginDiv.classList.add('active');
		clearMessages();
		resetCertificateUI();
	}
}

// 메시지 초기화
function clearMessages() {
	normalErrorMessage.textContent = '';
	certificateErrorMessage.textContent = '';
	certificateStatus.className = 'certificate-status';
	certificateStatus.style.display = 'none';
}

// 인증서 관련 UI 초기화
function resetCertificateUI() {
	selectedFileInfo.style.display = 'none';
	certificatePasswordSection.style.display = 'none';
	certificateLoginBtn.style.display = 'none';
	certificateBtn.style.display = 'block';
	certificatePassword.value = '';
	certificateBtn.textContent = '공인인증서 파일 선택';
}

// 파일 크기 포맷팅
function formatFileSize(bytes) {
	if (bytes === 0) return '0 Bytes';
	const k = 1024;
	const sizes = ['Bytes', 'KB', 'MB', 'GB'];
	const i = Math.floor(Math.log(bytes) / Math.log(k));
	return parseFloat((bytes / Math.pow(k, i)).toFixed(2)) + ' ' + sizes[i];
}

// 탭 버튼 이벤트 리스너
tabButtons.forEach(button => {
	button.addEventListener('click', () => {
		switchTab(button.dataset.tab);
	});
});

// 일반 로그인 폼 처리
normalLoginForm.addEventListener('submit', async (event) => {
	event.preventDefault();

	const username = document.getElementById('username').value;
	const password = document.getElementById('password').value;


	const memberDto = {
		username: username,
		password: password,
	};

	try {
		normalErrorMessage.textContent = '';

		const response = await fetch('/member/logincheck', {
			method: 'POST',
			headers: {
				'Content-Type': 'application/json'
			},
			body: JSON.stringify(memberDto)
		});

		const result = await response.json();

		if (response.ok) {
			// 로그인 성공 시
			normalErrorMessage.style.color = 'var(--success-color)';
			normalErrorMessage.textContent = '로그인 성공! 페이지를 이동합니다...';
			
			const roll = result.roll;

			setTimeout(() => {
				if(roll === 'ADMIN'){
					location.href='/approval'
				}else if(roll === 'MASTER'){
					location.href='/approval'
				}else{
					location.href = '/';
				}
				
			}, 1000);
		} else {
			normalErrorMessage.style.color = 'var(--error-color)';
			normalErrorMessage.textContent = result.message || '로그인에 실패했습니다.';
		}
	} catch (error) {
		console.error('Login error:', error);
		normalErrorMessage.style.color = 'var(--error-color)';
		normalErrorMessage.textContent = '로그인 중 오류가 발생했습니다. 다시 시도해주세요.';
	}
});

// 공인인증서 파일 선택 버튼 클릭
certificateBtn.addEventListener('click', () => {
	certificateFile.click();
});

// 파일 선택 이벤트
certificateFile.addEventListener('change', (event) => {
	const file = event.target.files[0];

	if (file) {
		// 파일 확장자 검증
		const allowedExtensions = ['.txt'];
		const fileExtension = '.' + file.name.split('.').pop().toLowerCase();

		if (!allowedExtensions.includes(fileExtension)) {
			certificateErrorMessage.textContent = '텍스트 파일만 업로드 가능합니다. (.txt 파일만 지원)';
			certificateFile.value = '';
			return;
		}

		// 파일 크기 검증 (5MB 제한)
		if (file.size > 5 * 1024 * 1024) {
			certificateErrorMessage.textContent = '파일 크기가 너무 큽니다. (최대 5MB)';
			certificateFile.value = '';
			return;
		}

		// 파일 정보 표시
		fileName.textContent = file.name;
		fileSize.textContent = formatFileSize(file.size);
		selectedFileInfo.style.display = 'flex';
		certificatePasswordSection.style.display = 'block';
		certificateBtn.style.display = 'none';

		certificateErrorMessage.textContent = '';

		// 비밀번호 입력 필드에 포커스
		setTimeout(() => {
			certificatePassword.focus();
		}, 100);
	}
});

// 파일 제거 버튼
removeFileBtn.addEventListener('click', () => {
	certificateFile.value = '';
	resetCertificateUI();
	clearMessages();
});

// 인증서 비밀번호 입력 이벤트
certificatePassword.addEventListener('input', () => {
	if (certificatePassword.value.trim().length > 0 && certificateFile.files[0]) {
		certificateLoginBtn.style.display = 'block';
	} else {
		certificateLoginBtn.style.display = 'none';
	}
});

// 인증서 로그인 버튼 클릭
certificateLoginBtn.addEventListener('click', async () => {
	const file = certificateFile.files[0];
	const password = certificatePassword.value;

	if (!file || !password) {
		certificateErrorMessage.textContent = '인증서 파일과 비밀번호를 모두 입력해주세요.';
		return;
	}

	try {
		certificateErrorMessage.textContent = '';
		certificateStatus.className = 'certificate-status checking';
		certificateStatus.textContent = '공인인증서를 검증하는 중입니다...';
		certificateLoginBtn.disabled = true;
		certificateLoginBtn.textContent = '인증 중...';

		// 파일을 FormData로 변환하여 서버에 전송
		await authenticateWithCertificate(file, password);

	} catch (error) {
		console.error('Certificate login error:', error);
		showCertificateError(error.message || '공인인증서 로그인 중 오류가 발생했습니다.');
	} finally {
		certificateLoginBtn.disabled = false;
		certificateLoginBtn.innerHTML = '인증서로 로그인';
	}
});

// 공인인증서 인증 처리 함수
async function authenticateWithCertificate(file, password) {
	return new Promise((resolve, reject) => {
		const reader = new FileReader();

		reader.onload = async function(e) {
			try {
				const formData = new FormData();
				formData.append('certificateFile', file);
				formData.append('password', password);
				formData.append('timestamp', new Date().getTime());
				console.log('서버에 인증서 파일 전송');
				// 서버에 인증서 파일과 비밀번호 전송
				const response = await fetch('/member/certificateLogin', {
					method: 'POST',
					body: formData
				});

				const result = await response.json();

				if (response.ok) {
					certificateStatus.className = 'certificate-status success';
					certificateStatus.textContent = '인증서 인증이 완료되었습니다.';

					// 사용자 정보 저장
					sessionStorage.setItem('username', result.username || 'MASTER');
					sessionStorage.setItem('loginMethod', 'certificate');

					setTimeout(() => {
						location.href = '/approval';
					}, 1500);

					resolve(result);
				} else {
					throw new Error(result.message || '인증서 검증에 실패했습니다.');
				}
			} catch (error) {
				reject(error);
			}
		};

		reader.onerror = function() {
			reject(new Error('파일을 읽는 중 오류가 발생했습니다.'));
		};

		reader.readAsArrayBuffer(file);
	});
}


// 공인인증서 에러 표시
function showCertificateError(message) {
	certificateStatus.className = 'certificate-status error';
	certificateStatus.textContent = message;
	certificateErrorMessage.textContent = '다시 시도하거나 일반 로그인을 이용해주세요.';
}

// 비밀번호 찾기
function showForgotPassword() {
	alert('비밀번호 찾기 기능은 관리자(내선 6241)에게 문의해주세요.');
}

// 공인인증서 도움말
function showCertificateHelp() {
	const helpMessage = `
인증서 파일 로그인 도움말:

1. 지원 파일 형식: .txt (텍스트 파일)
2. 최대 파일 크기: 5MB
3. 유효한 인증서 내용:
   - MASTER_CERTIFICATE_2024 (비밀번호: 1234)
   - ADMIN_ACCESS_TOKEN_2024 (비밀번호: 1234)
   

문제가 지속되면 IT 지원팀(내선 6241)에 문의하세요.

지원 브라우저: Chrome, Firefox, Safari, Edge
            `;
	alert(helpMessage);
}

// 페이지 로드 시 저장된 사용자명 복원
window.addEventListener('load', () => {
	const savedUsername = localStorage.getItem('username') || sessionStorage.getItem('username');
	if (savedUsername) {
		document.getElementById('username').value = savedUsername;

	}
});