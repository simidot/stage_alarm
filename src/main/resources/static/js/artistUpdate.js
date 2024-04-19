// 장르 목록을 받아오고 처리
let foundGenres = [];
let genreListHtml = '';
const genreTemplate = $('#genreList').html();
let selectedGenres = [];
let selectedGenreIds = [];
const artistTemplate = $('#updateArtistForm').html();
let artistListTemplate = '';

$(document).ready(function () {
    const path = window.location.pathname;
    const id = path.split('/').slice(-2, -1).pop();

    $.ajax({
        url: "/artist/" + id,
        type: "GET",
        contentType: "application/json; charset=utf-8",
        success: function (data) {


            let profileImgUrl = data.profileImg ? data.profileImg : 'https://stage-alarm.s3.ap-northeast-2.amazonaws.com/profileImg/user.png';
            let filledTemplate = artistTemplate.replace(/{{artist.id}}/g, data.id);
            filledTemplate = filledTemplate.replace(/{{profileImg}}/g, profileImgUrl);
            filledTemplate = filledTemplate.replace(/{{artist.name}}/g, data.name);
            filledTemplate = filledTemplate.replace(/{{artist.age}}/g, data.age);

            artistListTemplate += filledTemplate;
            $('#updateArtistForm').html(artistListTemplate);

            // 성별에 따라 라디오 버튼 상태 설정
            if (data.gender === 'M') {
                $('#male').prop('checked', true);
            } else if (data.gender === 'F') {
                $('#female').prop('checked', true);
            }

            $.ajax({
                url: "/genre/all",
                type: "GET",
                success: function (response) {
                    $.each(response, function (index, genre) {
                        let filledTemplate = genreTemplate.replace(/{{genre.id}}/g, genre.id); // artist.id를 템플릿에 추가
                        filledTemplate = filledTemplate.replace(/{{genre.name}}/g, genre.name);
                        filledTemplate = filledTemplate.replace(/{{genre.name}}/g, genre.name); // 버튼의 data-artist-name 속성에도 적용
                        genreListHtml += filledTemplate;
                        foundGenres.push(genre.name);
                    });

                    $('#genreList').html(genreListHtml);
                    // 아티스트 정보를 가져온 후에 선택된 장르를 설정
                    setSelectedGenres(data.genres);
                },
                error: function (xhr, status, error) {
                    console.error('Failed to load genres: ', error);
                    if (xhr.status === 403) {
                        alert("권한이 없습니다.");
                        window.history.back();
                    }
                }
            });
        }
    });


    // 선택된 장르를 설정하고, 해당 장르 버튼을 삭제 버튼으로 변경하는 함수
    function setSelectedGenres(genres) {
        selectedGenres = genres.map(genre => genre.name);
        selectedGenreIds = genres.map(genre => genre.id);
        $('#selectedGenreName').text(selectedGenres.join(', '));

        // 모든 장르 버튼을 초기 상태(선택 버튼)로 설정
        $('.genre-btn').each(function() {
            $(this).removeClass('remove-genre-btn').addClass('select-genre-btn').text('선택');
        });

        // 선택된 장르에 해당하는 버튼을 삭제 버튼으로 변경
        genres.forEach(genre => {
            console.log("선택된 장르 : ", genre.name);
            // data-genre-name 속성을 사용하여 해당 장르 버튼을 찾음
            const $genreButton = $(`.genre-btn[data-genre-name='${genre.name}']`);
            $genreButton.removeClass('select-genre-btn').addClass('remove-genre-btn').text('삭제');
        });
    }


// 삭제 버튼 클릭 이벤트 처리
    $(document).on('click', '.remove-genre-btn', function () {
        const genreName = $(this).data('genre-name');
        const genreId = $(this).data('genre-id');

        // 선택된 장르 목록에서 해당 장르 제거
        const index = selectedGenres.indexOf(genreName);
        if (index !== -1) {
            selectedGenres.splice(index, 1);
            selectedGenreIds.splice(index, 1);
        }
        // 선택된 장르 목록 다시 표시
        $('#selectedGenreName').text(selectedGenres.join(', '));
        $(this).removeClass('remove-genre-btn').addClass('select-genre-btn').text('선택');

    });

    $(document).on('click', '.select-genre-btn', function () {
        const genreName = $(this).data('genre-name');
        const genreId = $(this).data('genre-id');
        console.log('genreName -', genreName);
        // 이미 선택된 장르인지 확인
        if (!selectedGenres.includes(genreName)) {
            selectedGenres.push(genreName); // 배열에 아티스트 이름 추가
            selectedGenreIds.push(genreId);
        }
        console.log("selectedGenreIds : ", selectedGenreIds);
        // 선택된 모든 장르의 이름을 표시
        $('#selectedGenreName').text(selectedGenres.join(', ')); // 선택된 모든 장르 이름을 쉼표로 구분하여 표시
        $(this).removeClass('select-genre-btn').addClass('remove-genre-btn').text('삭제');
    });

    $('#updateArtistForm').on('submit', function (e) {
        e.preventDefault();
        if (!selectedGenres || selectedGenres.length === 0) {
            alert('장르를 최소 1가지 이상 선택해주세요.');
            return false;
        }

        const formData = new FormData();
        const profileImgInput = document.getElementById('image');
        formData.append('file', profileImgInput.files[0]);
        const dto = {
            id: $('#updateArtistForm input[name="id"]').val(),
            name: $('#name').val(),
            age: $('#age').val(),
            gender: $("input[name='gender']:checked").val(),
            genreIds: selectedGenreIds
        };

        // 나머지 폼 데이터 추가
        formData.append('dto', new Blob([JSON.stringify(dto)], {type: 'application/json'}));

        $.ajax({
            url: "/artist/" + id,
            type: "PATCH",
            contentType: false,
            processData: false,
            data: formData,
            success: function (response) {
                console.log("Success:", response);
                alert("아티스트 정보가 업데이트되었습니다.");
                window.location.href = "/subscribe";
            },
            error: function (xhr, status, error) {
                console.error("Error:", error);
                alert("게시글 업데이트에 실패했습니다.");
            }
        });
    });


    $("#image").on("change", function () {
        const fileInput = $("#image")[0];
        const files = fileInput.files;
        const reg = /(.*?)\.(jpg|bmp|jpeg|png|jfif|JPG|BMP|JPEG|PNG|JFIF)$/;
        const maxSize = 5 * 1024 * 1024;

        // var file = event.target.files[0];
        const imageContainer = $("#imageContainer");
        imageContainer.empty();

        for (let i = 0; i < files.length; i++) {
            const file = files[i];
            const reader = new FileReader();
            // reader.readAsDataURL(file);

            reader.onload = (function (file) {
                return function (e) {
                    // 미리보기 이미지의 크기 조절
                    const img = $("<img>").attr("src", e.target.result).css({
                        "max-width": "200px",
                        "max-height": "200px",
                        "margin": "5px"  // 이미지 간격을 조절하기 위한 스타일
                    });
                    // 이미지를 이미지 컨테이너에 추가
                    imageContainer.append(img);
                };
            })(file);

            if (!file.name.match(reg)) {
                alert("이미지 파일만 업로드 가능합니다. ");
                fileInput.value = "";
                return;
            } else if (file.size >= maxSize) {
                alert("파일 사이즈는 5MB까지 가능합니다. ");
                fileInput.value = "";
                return;
            }
            reader.readAsDataURL(file);
        }
    });
});