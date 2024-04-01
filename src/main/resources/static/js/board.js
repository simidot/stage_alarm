// URL에서 특정 부분을 추출하는 함수
function getPathParam(index) {
  const currentPath = window.location.pathname;
  const pathSegments = currentPath.split('/');
  return pathSegments[index];
}

// 댓글 컨텐츠 생성
function displayComments(commentList, depth = 0) {
  let commentsHtml = '';
  commentList.forEach(comment => {
    // 댓글과 대댓글 여백 구분
    const marginStyle = `style="margin-left: ${depth * 20}px"`;

    // 댓글 or 대댓글 HTML
    commentsHtml += `
      <div class="comment" ${marginStyle}>
        <p><strong>${comment.loginId}</strong></p>
        <p>${comment.content}</p>
        <p class="comment-date">${comment.createdAt}</p>
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
    <div class="comment" style="margin-left: ${comment.depth * 20}px">
      <p><strong>${comment.loginId}</strong></p>
      <p>${comment.content}</p>
      <p class="comment-date">${comment.createdAt}</p>
      <hr>
    </div>
  `;

  // 새 댓글을 댓글 섹션에 추가
  $('.comments-section').append(newCommentHtml);
}
