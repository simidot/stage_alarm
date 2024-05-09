$(document).ready(function () {

  $.ajax({
    type: 'GET',
    url: '/authenticated',
    success: function (response) {
      console.log('Permission granted');
      // 성공하면 그대로 진행시켜서 기존 로직이 흘러가게 한다.
    },
    error: function (xhr, status, error) {
      console.error('Permission denied:', xhr.responseText);
      alert('권한이 없습니다.');
      // 그림에서 3. reject 됐을때 바로 뒤로가는 부분
      location.href = '/' // 알림창을 띄우고 메인페이지로 이동
    }
  });

})