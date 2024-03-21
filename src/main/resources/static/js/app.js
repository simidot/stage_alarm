// header 토글 js
function toggleDropdown(dropdownId) {
    var dropdownContent = document.getElementById(dropdownId);

    if (dropdownContent.style.display === "block" || dropdownContent.style.display === "") {
        dropdownContent.style.display = "none";
    } else {
        dropdownContent.style.display = "block";
    }
}

// 다른 곳을 클릭하면 토글 닫기
window.onclick = function(event) {
    if (!event.target.matches('.dropdown button')) {
        var dropdowns = document.getElementsByClassName("dropdown-content");
        for (var i = 0; i < dropdowns.length; i++) {
            var openDropdown = dropdowns[i];
            if (openDropdown.style.display === "block") {
                openDropdown.style.display = "none";
            }
        }
    }
}

// sidebar 토글 js
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

