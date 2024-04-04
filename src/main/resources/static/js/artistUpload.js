$("#profileImg").on("change", function(){
    const fileInput = $("#profileImg")[0];
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
                var img = $("<img>").attr("src", e.target.result).css({
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

let foundGenres = [];
let genreListHtml= '';
const genreTemplate = $('#genreList').html();
let selectedGenres = [];
let selectedGenreIds = [];
$(document).ready(function () {
    $.ajax({
        url: "/genre",
        type: "GET",
        success: function (response) {
            // 장르 정보를 반복하여 HTML에 추가
            $.each(response, function (index, genre) {
                let filledTemplate = genreTemplate.replace(/{{genre.id}}/g, genre.id); // artist.id를 템플릿에 추가
                filledTemplate = filledTemplate.replace(/{{genre.name}}/g, genre.name);
                filledTemplate = filledTemplate.replace(/{{genre.name}}/g, genre.name); // 버튼의 data-artist-name 속성에도 적용
                genreListHtml += filledTemplate;
                foundGenres.push(genre.name);
            });

            // HTML에 추가된 아티스트 리스트를 추가
            $('#genreList').html(genreListHtml);
        },
        error: function (xhr, status, error) {
            console.error('Failed to load artists: ', error);
            if (xhr.status === 403) {
                alert("권한이 없습니다.");
                window.history.back();
            }
        }
    });
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

    // 선택된 모든 장르의 이름을 표시
    $('#selectedGenreName').text(selectedGenres.join(', ')); // 선택된 모든 장르 이름을 쉼표로 구분하여 표시
});

let checkedArtistName = null;
$('#checkArtist').on('click', function(e) {
    e.preventDefault();
    const artistName = $('#artistName').val();
    if (!artistName) {
        $('#artistFeedback').css('color', 'red').text('아티스트명을 입력하세요.');
        return;
    }
    // 서버에 아티스트 중복 검사 요청
    // 예시 URL 및 결과 처리 로직, 실제 구현에 맞게 수정 필요
    $.ajax({
        type: "POST",
        url: "/artist/artist-check",
        data: { "artistName": artistName },
        success: function(res) {
            if (!res) {
                $('#artistName').css('border-color', 'green');
                $('#artistFeedback').css('color', 'green').text('등록 가능한 아티스트입니다.');
                checkedArtistName = artistName;
            } else {
                $('#artistName').css('border-color', 'red');
                $('#artistFeedback').css('color', 'red').text('이미 등록된 아티스트입니다.');
                checkedArtistName = null;
            }
        }
    });
});


$('#artistForm').on('submit', function(e) {
    e.preventDefault();
    // 필수 입력 미입력시 제출 방지
    if (checkedArtistName ===null) {
        alert('아티스트 중복검사를 받아주세요.');
        $('#artistName').css('border-color', 'red');
        return false;
    } else if (!selectedGenres || selectedGenres.length === 0) {
        alert('장르를 최소 1가지 이상 선택해주세요.');
        return false;
    }

    // FormData 객체 생성
    const formData = new FormData();
    // 프로필 이미지 파일 추가
    const profileImgInput = document.getElementById('profileImg');
    formData.append('file', profileImgInput.files[0]);
    const gender = $("input[name='gender']:checked").val();


    // JSON 형식의 데이터 추가
    const artistData = {
        name: $('#artistName').val(),
        age: $('#age').val(),
        gender: gender,
        genreIds: selectedGenreIds
    };
    formData.append('dto', new Blob([JSON.stringify(artistData)], { type: 'application/json' })
    );


    $.ajax({
        type: 'POST',
        url: '/artist',
        contentType: false, // contentType을 false로 설정하여 jQuery가 자동으로 multipart/form-data 형식으로 요청을 설정하도록 합니다.
        processData: false,
        data: formData, // formData 객체를 직접 전달합니다.
        success: function (response) {
            console.log('아티스트 등록 성공 : ', response);
            location.href = '/subscribe';
        },
        error: function (xhr, status, error) {
            console.log('아티스트 등록 실패:', xhr.responseText);
            alert('아티스트 등록에 실패하였습니다. 다시 시도해주세요.');
            if (xhr.status === 403) {
                alert("권한이 없습니다.");
                window.history.back();
            }
        }
    })
});
