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

// 댓글 컨텐츠 생성
function displayComments(commentList, boardId, depth = 0) {
  let commentsHtml = '';
  commentList.forEach(comment => {
    // 댓글과 대댓글 여백 구분
    const marginStyle = `style="margin-left: ${depth * 50}px"`;

    // 댓글 or 대댓글 HTML
    commentsHtml += `
      <div id="comment-${comment.id}" class="comment" ${marginStyle}>
        <p><strong>${comment.loginId}</strong></p>
        <p id="comment-content-${comment.id}">${comment.content}</p>
        <div class="comment-footer" style="display: flex; justify-content: space-between">
          <div style="display: flex;">
            <p class="comment-date" style="margin-right: 5px">${comment.createdAt}</p>
            ${depth === 0 ? `<a type="button" onclick="addReply('${comment.id}', '${boardId}')">답글 쓰기</a>` : ''}
          </div>
          <div id="comment-actions-${comment.id}" style="margin-left: auto">             
            <button type="button" class="btn btn-secondary mr-2" onclick="editComment('${comment.id}')">수정</button>
            <button type="button" class="btn btn-secondary mr-2" onclick="deleteComment('${comment.id}')">삭제</button>
          </div>
        </div>
        <hr>
      </div>
    `;

    // 대댓글이 있는 경우 재귀적으로 처리
    if (comment.childComments.length > 0) {
      commentsHtml += displayComments(comment.childComments, boardId, depth + 1);
    }
  });
  return commentsHtml;
}

function addCommentToPage(comment, boardId) {
  // 새 댓글 HTML 생성
  const newCommentHtml = `
    <div id="comment-${comment.id}" class="comment">
      <p><strong>${comment.loginId}</strong></p>
      <p id="comment-content-${comment.id}">${comment.content}</p>
      <div class="comment-footer" style="display: flex; justify-content: space-between">
        <div style="display: flex;">
          <p class="comment-date" style="margin-right: 5px">${comment.createdAt}</p>
          <a type="button" onclick="addReply('${comment.id}', '${boardId}')">답글 쓰기</a>
        </div>
        <div id="comment-actions-${comment.id}" style="margin-left: auto"> 
          <button type="button" class="btn btn-secondary mr-2" onclick="editComment('${comment.id}')">수정</button>
          <button type="button" class="btn btn-secondary mr-2" onclick="deleteComment('${comment.id}')">삭제</button>
        </div>
      </div>
      <hr>
    </div>
  `;

  // 새 댓글을 댓글 섹션에 추가
  $('.comments-section').append(newCommentHtml);
}

// 댓글 수정 함수
function editComment(commentId) {
  // 기존 댓글 내용을 가져옴
  const commentContentElement = document.getElementById(`comment-content-${commentId}`);
  const originalContent = commentContentElement.innerHTML;

  // 수정과 삭제 버튼을 숨기고 저장 버튼을 추가
  const commentActionsElement = document.getElementById(`comment-actions-${commentId}`);
  commentActionsElement.style.display = 'none'; // 숨김 처리

  // textarea와 저장 버튼으로 변경
  const editContainer = document.createElement('div');
  editContainer.id = `edit-container-${commentId}`;
  editContainer.innerHTML = `
    <textarea id="edit-comment-${commentId}" class="input-container">${originalContent}</textarea>
    <button class="btn btn-primary mt-2" onclick="saveEditedComment('${commentId}')">저장</button>
  `;

  // 해당 댓글 위치에 삽입
  commentContentElement.parentNode.insertBefore(editContainer, commentContentElement);
  commentContentElement.style.display = 'none'; // 원래 댓글 내용 숨김 처리
}

function saveEditedComment(commentId) {
  // 수정된 내용 가져오기
  const editedContent = document.getElementById(`edit-comment-${commentId}`).value;
  // 수정된 내용 commentData에 담기
  const commentData = {
    content: editedContent
  };

  // update 요청
  $.ajax({
    url: `/board-comments/${commentId}`,
    type: 'PUT',
    contentType: 'application/json',
    data: JSON.stringify(commentData),
    success: function (response) {
      // 페이지에서도 내용 업데이트
      const commentContentElement = document.getElementById(`comment-content-${commentId}`);
      commentContentElement.innerHTML = editedContent;
      commentContentElement.style.display = 'block'; // 원래 댓글 내용 다시 보여줌

      // 수정 컨테이너와 저장 버튼 제거
      document.getElementById(`edit-container-${commentId}`).remove();

      // 수정과 삭제 버튼 다시 보여줌
      const commentActionsElement = document.getElementById(`comment-actions-${commentId}`);
      commentActionsElement.style.display = 'block';
    },
    error: function(xhr, status, error) {
      if (xhr.status === 403) {
        alert("권한이 없습니다.");
        window.history.back();
      }
    }
  });
}

// 댓글 삭제 함수
function deleteComment(commentId) {
  // 모달창 대신 alert 창 표시
  if (!confirm("댓글을 삭제하시겠습니까?")) {
    return; // 사용자가 취소를 누르면 여기서 함수 종료
  }
  // "확인"을 선택한 경우, AJAX 요청으로 댓글 삭제 처리
  $.ajax({
    url: `/board-comments/${commentId}`, // URL에 commentId 변수를 포함
    type: 'DELETE', // HTTP 메소드는 DELETE로 설정
    success: function(response) {
      // 댓글이 성공적으로 삭제되었을 때, 페이지 리디렉션 대신 DOM에서 해당 댓글 제거
      const commentElement = document.getElementById(`comment-${commentId}`);
      if (commentElement) {
        commentElement.parentNode.removeChild(commentElement);
      } else {
        alert("댓글을 찾을 수 없습니다.");
      }
    },
    error: function(xhr, status, error) {
      // 요청이 실패했을 때 실행할 코드
      if (xhr.status === 403) {
        alert("권한이 없습니다.");
        window.history.back();
      }
    }
  });
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

// ----------

// 답글 쓰기 클릭 시, 함수
function addReply(commentId, boardId) {
  // 이미 열린 답글 입력창이 있는지 확인
  const existingReplyBox = document.getElementById(`reply-box-${commentId}`);
  // 이미 답글 입력창이 있으면 추가 작업을 중단
  if (existingReplyBox) {
    return;
  }

  // 답글 입력창 생성
  const replyBox = document.createElement('div');
  replyBox.id = `reply-box-${commentId}`;
  replyBox.innerHTML = `
    <div style="display: flex; flex-direction: column; margin-left: 20px;">
      <textarea id="reply-content-${commentId}" class="form-control" rows="3" placeholder="댓글을 입력하세요"></textarea>
      <div class="reply-actions" style="margin-top: 10px; display: flex; justify-content: flex-end;">
        <button type="button" class="btn btn-secondary" onclick="cancelReply('${commentId}')">취소</button>
        <button type="button" class="btn btn-primary" onclick="saveReply('${boardId}', '${commentId}')">저장</button>
      </div>
    </div>
    <hr>
  `;

  // 답글 입력창을 댓글 아래에 추가
  const commentElement = document.getElementById(`comment-${commentId}`);
  commentElement.appendChild(replyBox);
}

// 답글 취소 함수
function cancelReply(commentId) {
  const replyBox = document.getElementById(`reply-box-${commentId}`);
  if (replyBox) {
    replyBox.remove(); // 답글 입력창 제거
  }
}

// 답글 저장 함수
function saveReply(boardId, commentId) {
  const replyContent = document.getElementById(`reply-content-${commentId}`).value;
  // 답글 내용을 서버에 저장하는 코드를 여기에 추가
  // 예시: axios.post('/api/comments', { commentId: commentId, content: replyContent })
  //       .then(response => { /* 성공 처리 */ })
  //       .catch(error => { /* 에러 처리 */ });

  const commentData = {
    content: replyContent
  };

  $.ajax({
    url: `/board-comments/${boardId}/reply/${commentId}`,
    type: 'POST',
    contentType: 'application/json',
    data: JSON.stringify(commentData),
    success: function (response) {
      addReplyCommentToPage(response, commentId);
    },
    error: function (xhr, textStatus, error) {
      // 토큰 만료 -> 재발급
      if (xhr.status === 401) {
        console.log("refreshToken");
        refreshToken(saveReply);
      } else if (xhr.status === 403) {
        alert("권한이 없습니다.");
        console.log(xhr.status);
        window.history.back();
      } else {
        console.error("오류: " + textStatus + ": " + error);
      }
    }
  })

  cancelReply(commentId); // 답글 입력창 제거
}

function addReplyCommentToPage(comment, parentCommentId) {
  // 새 대댓글 HTML 생성
  const newCommentHtml = `
    <div id="comment-${comment.id}" class="comment reply-to-${parentCommentId}" style="margin-left: ${comment.depth * 50}px">
      <p><strong>${comment.loginId}</strong></p>
      <p id="comment-content-${comment.id}">${comment.content}</p>
      <div class="comment-footer" style="display: flex; justify-content: space-between; align-items: center;">
        <p class="comment-date">${comment.createdAt}</p>
        <div id="comment-actions-${comment.id}">
          <button type="button" class="btn btn-secondary mr-2" onclick="editComment('${comment.id}')">수정</button>
          <button type="button" class="btn btn-secondary mr-2" onclick="deleteComment('${comment.id}')">삭제</button>
        </div>
      </div>
      <hr>
    </div>
  `;

  // 부모 댓글 아래에 있는 마지막 대댓글 찾기
  const lastReply = $(`.reply-to-${parentCommentId}`).last();

  // 마지막 대댓글이 있으면 그 다음에 새 대댓글 추가, 없으면 부모 댓글 바로 다음에 추가
  if (lastReply.length) {
    lastReply.after(newCommentHtml);
  } else {
    $(`#comment-${parentCommentId}`).after(newCommentHtml);
  }
}

