
function loadDataAndRender(sortParam) {
  const categoryId = getPathParam(2); // URL에서 categoryId 추출

  let requestURL = `/board/${categoryId}`;
  if (sortParam) {
    requestURL += `?sortParam=${sortParam}`;

    // URL에 쿼리스트링 추가
    const newUrl = `${window.location.protocol}//${window.location.host}${window.location.pathname}?sortParam=${sortParam}`;
    window.history.pushState({path:newUrl}, '', newUrl);
  }

  $.ajax({
    url: requestURL,
    type: 'GET',
    contentType: 'application/json',
    success: function(data) {
      const $boardList = $('.boardList');
      $boardList.empty(); // 기존 목록을 비웁니다.

      // 받아온 데이터로 게시글 목록 업데이트
      $.each(data.content, function(index, board) {
        const boardRow = `
                    <tr>
                        <td>${board.id}</td>
                        <td><a href="/boards/view/${board.id}" class="boardTitle" style="color: black">${board.title}</a></td>
                        <td>${board.writer}</td>
                        <td>${board.createdAt}</td>
                        <td>${board.views}</td>
                    </tr>
                `;
        $boardList.append(boardRow); // 생성한 행을 '.boardList'에 추가합니다.
      });
    },
    error: function(xhr, status, error) {
      console.error("요청 실패: ", status, error);
    }
  });
}