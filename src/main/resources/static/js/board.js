// URL에서 특정 부분을 추출하는 함수
function getPathParam(index) {
  const currentPath = window.location.pathname;
  const pathSegments = currentPath.split('/');
  return pathSegments[index];
}

// 댓글 컨텐츠 생성
function displayComments(commentList, boardId, depth = 0) {
  let commentsHtml = '';
  commentList.forEach(comment => {
    // 댓글과 대댓글 여백 구분
    const marginStyle = `style="margin-left: ${depth * 20}px"`;

// 댓글 or 대댓글 HTML
    commentsHtml += `
      <div id="comment-${comment.id}" class="comment" ${marginStyle}>
        <p><strong>${comment.loginId}</strong></p>
        <p id="comment-content-${comment.id}">${comment.content}</p>
        <div class="comment-footer" style="display: flex; justify-content: space-between; align-items: center;">
          <p class="comment-date">${comment.createdAt}</p>
          <div id="comment-actions-${comment.id}"> 
            <button type="button" onclick="editComment('${comment.id}')">수정</button>
            <button type="button" onclick="deleteComment('${comment.id}')">삭제</button>
          </div>
        </div>
        <hr>
      </div>
    `;

    // 대댓글이 있는 경우 재귀적으로 처리
    if (comment.childComments.length > 0) {
      commentsHtml += displayComments(comment.childComments, depth + 1);
    }
  });
  return commentsHtml;
}

function addCommentToPage(comment) {
  // 새 댓글 HTML 생성
  const newCommentHtml = `
    <div id="comment-${comment.id}" class="comment" style="margin-left: ${comment.depth * 20}px">
      <p><strong>${comment.loginId}</strong></p>
      <p id="comment-content-${comment.id}">${comment.content}</p>
      <div class="comment-footer" style="display: flex; justify-content: space-between; align-items: center;">
        <p class="comment-date">${comment.createdAt}</p>
        <div>
          <button type="button" onclick="editComment('${comment.id}')">수정</button>
          <button type="button" onclick="deleteComment('${comment.id}')">삭제</button>
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
    <button onclick="saveEditedComment('${commentId}')">저장</button>
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
    url: `/comments/rewriting/${commentId}`,
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
      console.error('댓글 작성 실패:', status, error);
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
    url: `/comments/trash/${commentId}`, // URL에 commentId 변수를 포함
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
      console.error('댓글 삭제 실패', status, error);
      alert('댓글 삭제에 실패하였습니다.');
    }
  });
}
