$("#posterImage").on("change", function(){
    const fileInput = $("#posterImage")[0];
    const files = fileInput.files;
    const reg = /(.*?)\.(jpg|bmp|jpeg|png|JPG|BMP|JPEG|PNG)$/;
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

// 기존에 추가된 아티스트 목록을 저장할 배열
let foundArtists = [];
let foundGenres = [];
let artistListHtml = '';
let genreListHtml= '';
const artistTemplate = $('#artistList').html();
const genreTemplate = $('#genreList').html();
let selectedArtists = []; // 선택된 아티스트 이름을 저장할 배열
let selectedArtistIds = [];
let selectedGenres = [];
let selectedGenreIds = [];

$(document).ready(function () {
    $.ajax({
        url: "/artist/all",
        type: "GET",
        success: function (response) {
            $.each(response, function (index, artist) {
                let genresHtml = '';
                let genreIdsHtml = ''; // 장르의 ID를 저장할 배열
                $.each(artist.genres, function (index, genre) {
                    genresHtml += genre.name + ', '; // 장르의 이름을 문자열로 추가
                    genreIdsHtml += genre.id + ', '; // 장르의 ID를 배열에 추가
                });
                // 마지막 쉼표 제거
                genresHtml = genresHtml.slice(0, -2);
                console.log("genresHtml : ", genresHtml);

                genreIdsHtml = genreIdsHtml.slice(0, -2);
                console.log("gerneIdsString : ", genreIdsHtml);

                // 장르의 ID들을 쉼표로 연결하여 문자열로 저장
                // let genresIdsString = genresIds.join(',');
                let filledTemplate = artistTemplate.replace(/{{artist.id}}/g, artist.id)
                    .replace(/{{artist.name}}/g, artist.name)
                    .replace(/{{artist.name}}/g, artist.name)
                    .replace(/{{artist.genres}}/g, genresHtml)
                    .replace(/{{artist.genreIds}}/g, genreIdsHtml); // 장르의 ID 문자열을 치환
                console.log("genresHtml : ", genresHtml);
                console.log("gerneIdsString : ", genreIdsHtml);
                artistListHtml += filledTemplate;
                foundArtists.push(artist.name);
            });

            // HTML에 추가된 아티스트 리스트를 추가
            $('#artistList').html(artistListHtml);
        },
        error: function(xhr, status, error) {
            console.error('Failed to load artists: ', error);
        }
    });

    document.getElementById("searchInput").addEventListener("input", function() {
        const searchValue = this.value.toLowerCase();
        const artistItems = document.getElementsByClassName("artist-item");

        for (let i = 0; i < artistItems.length; i++) {
            const artistName = artistItems[i].getElementsByTagName("span")[0].textContent.toLowerCase();

            if (artistName.includes(searchValue)) {
                artistItems[i].style.display = "flex";
            } else {
                artistItems[i].style.display = "none";
            }
        }
    });

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
        error: function(xhr, status, error) {
            console.error('Failed to load artists: ', error);
        }
    });
});

$(document).on('click', '.select-artist-btn', function () {
    const artistName = $(this).data('artist-name');
    const artistId = $(this).data('artist-id');
    let artistGenresData = $(this).data('artist-genres');
    let artistGenreIdsData = $(this).data('artist-genreIds');

    let artistGenres = artistGenresData ? artistGenresData.split(',') : [];
    let artistGenreIds = artistGenreIdsData ? artistGenreIdsData.split(',') : [];

    // 아티스트 중복 체크
    const isDuplicateArtist = selectedArtists.some(artist => artist.id === artistId);

    if (!isDuplicateArtist) {
        console.log("중복되지 않은 아티스트: ", artistName);
        selectedArtists.push({name: artistName, id: artistId});

        // 이 아티스트의 모든 장르를 선택된 장르 배열에 추가
        artistGenres.forEach((genre, index) => {
            const genreId = artistGenreIds[index]?.trim();
            if (genreId) {
                genre = genre.trim();

                const isDuplicateGenre = selectedGenres.some(selectedGenre => selectedGenre.id === genreId);
                if (!isDuplicateGenre) {
                    console.log("중복되지 않은 장르: ", genre);
                    selectedGenres.push({name: genre, id: genreId});
                }
            }
        });
    }

    $('#selectedGenreName').text(selectedGenres.map(genre => genre.name).join('\n '));
    $('#selectedArtistName').text(selectedArtists.map(artist => artist.name).join('\n '));
});

$(document).on('click', '.select-genre-btn', function () {
    const genreName = $(this).data('genre-name');
    const genreId = $(this).data('genre-id');

    // 장르 중복 체크
    const isDuplicateGenre = selectedGenres.some(genre => genre.id === genreId);

    if (!isDuplicateGenre) {
        console.log('중복되지 않은 장르: ', genreName);
        selectedGenres.push({
            name: genreName,
            id: genreId
        });
    }

    $('#selectedGenreName').text(selectedGenres.map(genre => genre.name).join('\n'));
});


$('#addArtistBtn').click(function() {
    $('#addArtistForm').toggle(); // 입력 폼을 토글하여 보이기/숨기기
});

$('#addNewArtistBtn').click(function() {
    const newArtistName = $('#newArtistName').val();

    if (newArtistName === '') {
        alert('아티스트 이름을 입력하세요.');
        return;
    }
    if (foundArtists.includes(newArtistName)) {
        alert('이미 존재하는 아티스트입니다.');
        return;
    }
    // FormData 객체 생성
    const formData = new FormData();

    // DTO 객체 생성 (예시 데이터)
    const artistRequestDto = {
        name: newArtistName
    };
    console.log("new artist", newArtistName);

// DTO를 JSON 문자열로 변환하여 FormData에 추가
    formData.append("dto", new Blob([JSON.stringify(artistRequestDto)], {
        type: "application/json"
    }));

    // 새 아티스트를 리스트에 추가하는 작업을 수행
    $.ajax({
        url: "/artist",
        type: "POST",
        data: formData,
        contentType: false,
        processData: false,
        success: function (response) {

            foundArtists.push(newArtistName);
            $('#newArtistName').val('');

            let filledTemplate = artistTemplate.replace(/{{artist.id}}/g, response.id); // artist.id를 템플릿에 추가
            filledTemplate = filledTemplate.replace(/{{artist.name}}/g, response.name);
            filledTemplate = filledTemplate.replace(/{{artist.name}}/g, response.name); // 버튼의 data-artist-name 속성에도 적용
            artistListHtml += filledTemplate;
            console.log('새 아티스트 추가:', newArtistName);
            $('#artistList').html(artistListHtml);

        },
        error: function (xhr, status, error) {
            console.error('Failed to load artists: ', error);
        }
    });
});

$('#showInfoForm').on('submit', function (e) {
    e.preventDefault();
    if (selectedArtists.length === 0 || selectedGenres.length === 0) {
        alert('아티스트와 장르는 최소 1가지 선택해주세요. ');
        return false;
    }

    const formData = new FormData();
    const posterImgInput = document.getElementById('posterImage');
    formData.append('file', posterImgInput.files[0]);
    const genreIds = selectedGenres.map(genre => genre.id);

    const showData = {
        title: $('#title').val(),
        location: $('#place').val(),
        date: $('#date').val(),
        startTime: $('#startTime').val(),
        hours: $('#hours').val(),
        duration: $('#duration').val(),
        ticketVendor: $('#ticketVendor').val(),
        price: $('#price').val(),
        artists: selectedArtistIds,
        genres: genreIds
    };
    formData.append('dto', new Blob([JSON.stringify(showData)], {type: 'application/json'}));

    $.ajax({
        type: 'POST',
        url: '/show',
        contentType: false,
        processData: false,
        data: formData,
        success: function (response) {
            console.log('공연 정보 등록 성공 : ', response);
            location.href = '/shows';
        },
        error: function (xhr, status, error) {
            console.log('공연정보 등록 실패 : ', xhr.responseText);
            alert('공연정보 등록에 실패했습니다. 다시 시도해주세요. ');
        }
    })
});
