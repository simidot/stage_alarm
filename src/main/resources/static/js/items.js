$(document).ready(function () {
    fetchItems(0);

    let previousState = null;

    window.addEventListener('popstate', function (event){
        if (event.state!=null) {
            renderItems(event.state.data);
            renderPagination(event.state.data);
        } else {
            window.history.back();
        }
    })

    // 쿠키 값을 읽는 함수
    function getCookie(name) {
        let cookieValue = null;
        if (document.cookie && document.cookie !== '') {
            const cookies = document.cookie.split(';');
            for (let i = 0; i < cookies.length; i++) {
                const cookie = cookies[i].trim();
                // 이 쿠키가 찾고 있는 쿠키인지 확인
                if (cookie.substring(0, name.length + 1) === (name + '=')) {
                    cookieValue = decodeURIComponent(cookie.substring(name.length + 1));
                    break;
                }
            }
        }
        return cookieValue;
    }



    let itemFilledTemplate = null;

    function fetchItems(page) {
        // 쿠키 삭제하기 위해 과거 시간 설정
        // 쿠키에서 showInfoId 값 읽기
        const showInfoId = getCookie("showInfoId");
        let url = "";
        if (showInfoId==null){
            url = "/items";
        } else {
            url = "/" + showInfoId + "/items";
        }
        document.cookie = "showInfoId=; expires=Thu, 01 Jan 1970 00:00:00 UTC; path=/;";

        const size = 9;
        $.ajax({
            url: url,
            type: "GET",
            data: {
                "page": page,
                "size": size
            },
            success: function(data) {
                renderItems(data);
                renderPagination(data);

                previousState = {
                    pagination: page,
                    data: data
                };
                window.history.pushState(previousState, '', window.location.pathname);
            },
            error: function (xhr, status, error) {
                console.error("아이템 AJAX 요청 실패:", status, error);
                if (xhr.status === 403) {
                    alert("권한이 없습니다.");
                    window.history.back();
                }
            }
        });
    }

    const itemTemplate = $('#itemCardTemplate').html(); // 올바른 선택자 사용

    function renderItems(data) {
        const $itemList = $('#itemList .row');
        $itemList.empty();

        data.content.forEach(function (item, index){
            const itemFilledTemplate = fillItemTemplate(itemTemplate, item); // 함수가 템플릿을 반환하도록 수정
            if (index%3===0) {
                $itemList.append('<div class="w-100"></div>');
            }
            $itemList.append(itemFilledTemplate);

        });
    }

    function fillItemTemplate(template, item) {
        const defaultImageUrl = 'https://stage-alarm.s3.ap-northeast-2.amazonaws.com/defaultImg/bucket.png';

        const filledTemplate = template.replace(/{{item.itemImage}}/g, item.itemImg !== '' ? item.itemImg : defaultImageUrl)
            .replace(/{{item.name}}/g, item.name)
            .replace(/{{item.showName}}/g, item.showName)
            .replace(/{{item.price}}/g, item.price)
            .replace(/{{item.amount}}/g, item.amount)
            .replace(/{{item.id}}/g, item.id);

        return filledTemplate; // 채워진 템플릿을 반환
    }
    function renderPagination(data) {
        const $pagination = $('.pagination');
        $pagination.empty();

        // 페이지네이션 생성
        const totalPages = data.totalPages;
        console.log("totalPages", totalPages);
        const currentPage = data.pageable.pageNumber + 1;
        console.log("currentPage", currentPage);
        const pagesToShow = 10; // 한 번에 보여줄 페이지 번호 개수

        const startPage = Math.floor((currentPage - 1) / pagesToShow) * pagesToShow + 1;
        const endPage = Math.min(startPage + pagesToShow - 1, totalPages);

        const $prevPageItem = $('<li class="page-item"><a class="page-link" href="#" aria-label="Previous"><span aria-hidden="true">&lt;</span></a></li>');
        $prevPageItem.click(function () {
            if (!$(this).hasClass('disabled')) {
                fetchItems(currentPage - 2);
            }
        });
        if (currentPage === 1) {
            $prevPageItem.addClass('disabled');
        }
        $pagination.append($prevPageItem);

        for (let i = startPage; i <= endPage; i++) {
            const $pageItem = $('<li class="page-item"><a class="page-link" href="#">' + i + '</a></li>');
            if (i === currentPage) {
                $pageItem.addClass('active');
            }
            $pageItem.click(function () {
                if (!$(this).hasClass('active')) {
                    fetchItems(i - 1);
                }
            });
            $pagination.append($pageItem);
        }

        const $nextPageItem = $('<li class="page-item"><a class="page-link" href="#" aria-label="Next"><span aria-hidden="true">&gt;</span></a></li>');
        $nextPageItem.click(function () {
            if (!$(this).hasClass('disabled')) {
                fetchItems(currentPage);
            }
        });
        if (currentPage === totalPages) {
            $nextPageItem.addClass('disabled');
        }
        $pagination.append($nextPageItem);
    }
});