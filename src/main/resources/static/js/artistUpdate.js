$(document).ready(function () {
    const path = window.location.pathname;
    const id = path.split('/').slice(-2, -1).pop();


    $.ajax({
        url: "/artist/" + id,
        type: "GET",
        contentType: "application/json; charset=utf-8",
        success: function (data) {

            const artistHtml = `
                    <form id="updateArtistForm" enctype="multipart/form-data">
                        <div class="form-group mt-3">
              <div class="bold-label text-center mb-4" style="font-size: 20px;">아티스트 정보 업데이트</div>
              <div class="light-label"> 아티스트 정보를 입력해주세요. </div>
              <div class="row g-3">
              <div class="col-md-6">
                <div class="input-group">
                <img src=${data.profileImg ? data.profileImg : 'https://stage-alarm.s3.ap-northeast-2.amazonaws.com/profileImg/user.png'} class="rounded-circle img-thumbnail mb-3" alt="Artist Profile Picture" style="width:200px; height:250px">
                            <div class="card-body position-relative">
                                <input type="hidden" name="id" value="${data.id}">
                                <label for="name">아티스트명</label>
                                <input type="text" id="name" name="name" class="form-control" value="${data.name}" readonly>
                                <label for="age">나이</label>
                                <input type="number" class="form-control" id="age" name="age" min="1" max="150"
                       placeholder="아티스트 나이 (0~150)"  value="${data.age}">
                                <label for="gender">성별</label>
                        <div class="form-check ml-2">
                            <input class="form-check-input" type="radio" name="gender" id="male" value="M" ${data.gender === 'M' ? 'checked' : ''}>
                            <label class="form-check-label" for="male">남성</label>
                        </div>
                        <div class="form-check ml-2">
                            <input class="form-check-input" type="radio" name="gender" id="female" value="F" ${data.gender === 'F' ? 'checked' : ''}>
                            <label class="form-check-label" for="female">여성</label>
                        </div>
                                <label for="image">다른 프로필로 변경</label>
                                <input type="file" id="image" name="profileImg" class="form-control" accept="image/*" placeholder="아티스트 프로필">
                                <div id="imageContainer"></div>
                            </div>
                        </div>
                                  <div class="border mt-5 mb-5"></div> <!--구분선 넣기 -->
          <div class="light-label"> 해당 아티스트의 장르를 선택해주세요. <span class="required">*</span></div>
<div class="container">
            <div class="row">
              <!-- 왼쪽: 선택한 장르 -->
              <div class="col-md-6">
                <div class="light-label">선택한 장르</div>
                <div id="selectedGenre"><span id="selectedGenreName"></span></div>
              </div>
              <div class="col-md-6">
                <div class="light-label">전체 장르 리스트</div>
                <div id="genreList" style="height: 300px; overflow-y: auto;">
                  <!-- 각 장르 요소에 genre.id를 hidden으로 추가 -->
                  <!-- 선택 버튼에도 data-artist-id 속성을 추가하여 각 아티스트의 id를 저장 -->
                  <div class="genre-item light-label" style="display: flex; justify-content: space-between;">
                    <span class="light-label">{{genre.name}}</span> <!-- 장르 이름 -->
                    <input type="hidden" class="genre-id" value="{{genre.id}}">
                    <button type="button" class="btn btn-outline-primary select-genre-btn light-label" data-genre-id="{{genre.id}}" data-genre-name="{{genre.name}}">선택</button> <!-- 선택 버튼 -->
                  </div>
                </div>
              </div>

            </div>
          </div>
          <div class="border mt-5 mb-5"></div> <!--구분선 넣기 -->
          <button type="button" class="btn btn-dark save">저장</button>
                    </form>
                `;

            $('.artistInfo').append(artistHtml);
        }
    })

    $(document).on('click', '.save', function () {
        const form = document.getElementById('updateShowForm');
        const formData = new FormData(form);

        // 이미지 파일이 있는 경우에만 FormData에 추가
        if ($('#image')[0].files.length > 0) {
            formData.append('file', $('#image')[0].files[0]);
        }

        const dto = {
            id: $('#updateShowForm input[name="id"]').val(),
            title: $('#title').val(),
            date: $('#date').val(),
            location: $('#location').val(),
            startTime: $('#startTime').val(),
            ticketVendor: $('#ticketVendor').val(),
            duration: $('#duration').val()
        }

        const json = JSON.stringify(dto);
        const blob = new Blob([json], { type: 'application/json' });

        // 나머지 폼 데이터 추가
        formData.append('dto', blob);

        $.ajax({
            url: "/show/" + id,
            type: "PATCH",
            contentType: false,
            processData: false,
            data: formData,
            timeout: 600000,
            success: function (response) {
                console.log("Success:", response);
                alert("게시글이 업데이트되었습니다.");
                window.location.href = "/shows";
            },
            error: function (xhr, status, error) {
                console.error("Error:", error);
                alert("게시글 업데이트에 실패했습니다.");
            }
        });
    });
})