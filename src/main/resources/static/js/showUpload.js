
// 기존에 추가된 아티스트 목록을 저장할 배열
let foundArtists = [];
let foundGenres = [];
let artistListHtml = '';
let genreListHtml= '';
const artistTemplate = $('#artistList').html();
const genreTemplate = $('#genreList').html();
let selectedArtists = []; // 선택된 아티스트 이름을 저장할 배열
let selectedGenres = [];

$(document).ready(function () {
    $.ajax({
        url: "/artist/all",
        type: "GET",
        success: function (response) {
            $.each(response, function (index, artist) {
                console.log("artist genre: ", artist.genres);
                let genresHtml = '';
                $.each(artist.genres, function (index, genre) {
                    console.log("genre name: ", genre.toString());
                    genresHtml += genre + ', '; // 장르의 이름을 문자열로 추가
                });
                // // 마지막 쉼표 제거
                genresHtml = genresHtml.slice(0, -2);
                console.log("genresHtml : ", genresHtml);

                // 장르의 ID들을 쉼표로 연결하여 문자열로 저장
                // let genresIdsString = genresIds.join(',');
                let filledTemplate = artistTemplate.replace(/{{artist.id}}/g, artist.id)
                    .replace(/{{artist.name}}/g, artist.name)
                    .replace(/{{artist.name}}/g, artist.name)
                    .replace(/{{artist.genres}}/g, genresHtml);
                artistListHtml += filledTemplate;
                foundArtists.push(artist.name);
            });

            // HTML에 추가된 아티스트 리스트를 추가
            $('#artistList').html(artistListHtml);
        },
        error: function(xhr, status, error) {
            console.error('Failed to load artists: ', error);
            if (xhr.status === 403) {
                alert("권한이 없습니다.");
                window.history.back();
            }
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
        url: "/genre/all",
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
            if (xhr.status === 403) {
                alert("권한이 없습니다.");
                window.history.back();
            }
        }
    });
});

$(document).on('click', '.select-artist-btn', function () {
    const artistName = $(this).data('artist-name');
    const artistId = $(this).data('artist-id');
    let artistGenresData = $(this).data('artist-genres');
    // let artistGenreIdsData = $(this).data('artist-genreIds');

    let artistGenres = artistGenresData ? artistGenresData.split(',') : [];
    // let artistGenreIds = artistGenreIdsData ? artistGenreIdsData.split(',') : [];

    // 아티스트 중복 체크
    const isDuplicateArtist = selectedArtists.some(artist => artist.name === artistName);

    if (!isDuplicateArtist) {
        console.log("중복되지 않은 아티스트: ", artistName);
        console.log("중복되지 않은 아티스트의 장르: ", artistGenresData);
        selectedArtists.push({name: artistName, id: artistId});

        // 이 아티스트의 모든 장르를 선택된 장르 배열에 추가
        artistGenres.forEach((genre, index) => {
            const genreName = artistGenres[index]?.trim();
            if (genreName) {
                genre = genre.trim();

                const isDuplicateGenre = selectedGenres.some(selectedGenre => selectedGenre.name === genreName);
                if (!isDuplicateGenre) {
                    console.log("중복되지 않은 장르: ", genre);
                    selectedGenres.push({name: genre});
                    console.log("")
                }
            }
        });
    }

    $('#selectedGenreName').text(selectedGenres.map(genre => genre.name).join('\n '));
    $('#selectedArtistName').text(selectedArtists.map(artist => artist.name).join('\n '));
});

$(document).on('click', '.select-genre-btn', function () {
    const genreName = $(this).data('genre-name');

    // 장르 중복 체크
    const isDuplicateGenre = selectedGenres.some(genre => genre.name === genreName);

    if (!isDuplicateGenre) {
        console.log('중복되지 않은 장르: ', genreName);
        selectedGenres.push({
            name: genreName,
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
        name: newArtistName,
        genreIds: []
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
            filledTemplate = filledTemplate.replace(/{{artist.genres}}/g, null);
            artistListHtml += filledTemplate;
            console.log('새 아티스트 추가:', newArtistName);
            $('#artistList').html(artistListHtml);

        },
        error: function (xhr, status, error) {
            console.error('Failed to add artists: ', error);
            if (xhr.status === 403) {
                alert("권한이 없습니다.");
                window.history.back();
            }
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
    const posterImgInput = document.getElementById('image');
    formData.append('file', posterImgInput.files[0]);
    const genreNames = selectedGenres.map(genre => genre.name);
    const artistIds = selectedArtists.map(artist => artist.id);

    const showData = {
        title: $('#title').val(),
        location: $('#place').val(),
        date: $('#date').val(),
        startTime: $('#startTime').val(),
        hours: $('#hours').val(),
        duration: $('#duration').val(),
        ticketVendor: $('#ticketVendor').val(),
        price: $('#price').val(),
        artists: artistIds,
        genres: genreNames
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
            if (xhr.status === 403) {
                alert("권한이 없습니다.");
                window.history.back();
            }
        }
    })
});
