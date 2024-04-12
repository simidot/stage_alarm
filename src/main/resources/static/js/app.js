// header 토글 js
function toggleDropdown(dropdownId) {
    const dropdownContent = document.getElementById(dropdownId);

    if (dropdownContent.style.display === "block" || dropdownContent.style.display === "") {
        dropdownContent.style.display = "none";
    } else {
        dropdownContent.style.display = "block";
    }
}

// 다른 곳을 클릭하면 토글 닫기
window.onclick = function(event) {
    if (!event.target.matches('.dropdown button')) {
        const dropdowns = document.getElementsByClassName("dropdown-content");
        for (let i = 0; i < dropdowns.length; i++) {
            const openDropdown = dropdowns[i];
            if (openDropdown.style.display === "block") {
                openDropdown.style.display = "none";
            }
        }
    }
}

// sidebar 토글 js
// admin
function toggleAdminSubMenu() {
    const adminShowSubMenu = document.getElementById('adminShowSubMenu');
    adminShowSubMenu.style.display = (adminShowSubMenu.style.display === 'none' || adminShowSubMenu.style.display === '') ? 'block' : 'none';
}

function toggleAdminUploadMenu() {
    const adminUploadSubMenu = document.getElementById('adminUploadSubMenu');
    adminUploadSubMenu.style.display = (adminUploadSubMenu.style.display === 'none' || adminUploadSubMenu.style.display === '') ? 'block' : 'none';
}

// user
function toggleMySubMenu() {
    const myPageSubMenu = document.getElementById('myPageSubMenu');
    myPageSubMenu.style.display = (myPageSubMenu.style.display === 'none' || myPageSubMenu.style.display === '') ? 'block' : 'none';
}

function toggleComSubMenu() {
    const communitySubMenu = document.getElementById('communitySubMenu');
    communitySubMenu.style.display = (communitySubMenu.style.display === 'none' || communitySubMenu.style.display === '') ? 'block' : 'none';
}

function toggleShowSubMenu() {
    const showSubMenu = document.getElementById('showSubMenu');
    showSubMenu.style.display = (showSubMenu.style.display === 'none' || showSubMenu.style.display === '') ? 'block' : 'none';
}

// 비로그인 유저
function toggleUnauthShowSubMenu() {
    const unauthShowSubMenu = document.getElementById('unauthShowSubMenu');
    unauthShowSubMenu.style.display = (unauthShowSubMenu.style.display === 'none' || unauthShowSubMenu.style.display === '') ? 'block' : 'none';
}

function toggleUnauthComSubMenu() {
    const unauthCommunitySubMenu = document.getElementById('unauthCommunitySubMenu');
    unauthCommunitySubMenu.style.display = (unauthCommunitySubMenu.style.display === 'none' || unauthCommunitySubMenu.style.display === '') ? 'block' : 'none';
}

// Ajax 전역 설정
$.ajaxSetup({
    beforeSend: function(xhr) {
        const jwtToken = localStorage.getItem('jwtToken');

        if (localStorage.getItem('jwtToken')) {
            xhr.setRequestHeader('Authorization', `Bearer ${jwtToken}`);
        }
    }
});


// 리프레쉬토큰으로 토큰 재발급 코드
function refreshToken(callback) {
    // 쿠키에서 리프레시 토큰 ID 추출 및 서버에 전송하여 새 액세스 토큰 요청
    $.ajax({
        url: "/auth/refresh",
        type: "POST",
        beforeSend: null,  // 전역 beforeSend 설정을 무시
        success: function(data) {
            const token = data.token;
            localStorage.setItem('jwtToken', token); // 새 토큰 저장
            callback(); // 사용자 정보 재로딩
        },
        error: function(xhr) { // 리프레시도 만료

            if (xhr.status === 403) {
                console.error("403");
                window.location.href = '/user/login'; // 로그인 페이지로 이동
            } else {
                console.error("오류 발생");
                window.location.href = '/user/login'; // 로그인 페이지로 이동
            }
        }
    });
}