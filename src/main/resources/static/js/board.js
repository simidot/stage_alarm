// URL에서 특정 부분을 추출하는 함수
function getPathParam(index) {
  const currentPath = window.location.pathname;
  const pathSegments = currentPath.split('/');
  return pathSegments[index];
}

// 데이터 로드 및 페이지에 렌더링하는 함수
function loadData(pageNumber, sortParam) {
  const categoryId = getPathParam(2); // URL에서 categoryId 추출

  $.ajax({
    url: `/boards/${categoryId}`,
    type: 'GET',
    contentType: 'application/json',
    data: {
      page: pageNumber,
      size: 20,
      sortParam: sortParam
    },
    success: function (data) {
      const $boardList = $('.boardList');
      const $pagination = $('.pagination');
      // 초기화
      $boardList.empty();
      $pagination.empty();

      // 받아온 데이터로 게시글 목록 업데이트
      $.each(data.content, function(index, board) {
        const boardRow = `
                    <tr>
                        <td>${board.id}</td>
                        <td><a href="/board/view/${board.id}" class="boardTitle" style="color: black">${board.title}</a></td>
                        <td>${board.writer}</td>
                        <td>${board.createdAt}</td>
                        <td>${board.views}</td>
                    </tr>
                `;
        $boardList.append(boardRow); // 생성한 행을 '.boardList'에 추가합니다.
      });

      // 페이지네이션 생성
      const totalPages = data.totalPages;

      for (let i = 0; i < totalPages; i++) {
        const pageNumber = i + 1;
        const $pageItem = $('<li class="page-item"><a class="page-link" href="#" style="color: black">' + pageNumber + '</a></li>');
        $pageItem.click(function (e) {
          e.preventDefault();
          loadData(i, sortParam);
        });
        $pagination.append($pageItem);
        console.log("pagination "+$pagination.html());
      }
    },
    error: function(xhr, status, error) {
      if (xhr.status === 403) {
        alert("권한이 없습니다.");
        window.history.back();
      }
    }
  })
}

// 기존 이미지 미리보기 및 삭제 이벤트 리스너 설정 함수
function setupExistingImagePreviewAndDelete(imageUrl, deletedImageNames) {
  const preview = document.getElementById('image-preview');
  const imageUrlParts = imageUrl.split('/');
  const imageName = imageUrlParts[imageUrlParts.length - 1];
  const imgId = imageName.split('.')[0];
  const imgElement = `<div id="${imgId}" class="img-container"><img src="${imageUrl}" alt="Image preview"><button class="delete-img-btn">X</button></div>`; // 전체 URL을 src 속성에 사용
  preview.insertAdjacentHTML('beforeend', imgElement);

  // 기존 이미지 삭제 버튼 이벤트 리스너 추가
  document.getElementById(imgId).querySelector('.delete-img-btn').addEventListener('click', function() {
    document.getElementById(imgId).remove();
    // // existingImageArr에서 해당 이미지 URL 삭제
    // existingImageArr = existingImageArr.filter(e => e !== imageUrl);
    deletedImageNames.push(imgId);
    console.log("deleteImageNames: ", deletedImageNames)
  });
}

// 기존 이미지 정보를 가져오는 함수 수정
async function fetchPostData(boardId) {
  let existingImageUrls = []; // 이미지 URL을 저장할 배열

  const fetchData = () => {
    return new Promise((resolve, reject) => {
      $.ajax({
        url: `/boards/detail/${boardId}`,
        type: 'GET',
        success: function(response) {
          // 제목, 카테고리, 내용 필드 채우기
          $('#title-input').val(response.title);
          $('#board-type').val(response.categoryId);
          $('.input-container').val(response.content);

          // 이미지를 existingImageUrls 배열에 넣기
          response.imageList.forEach(function(image) {
            existingImageUrls.push(image.imgUrl); // 전체 이미지 URL을 배열에 추가
          });
          resolve(existingImageUrls);
        },
        error: function(xhr, status, error) {
          if (xhr.status === 403) {
            alert("권한이 없습니다.");
            window.history.back();
          }
        }
      });
    });
  };

  try {
    return await fetchData();
  } catch(error) {
    console.error("An error occurred:", error);
    return [];
  }
}
