// Ajax로 아티스트 목록을 가져와서 테이블에 추가하는 코드 작성
$(document).ready(function () {
    $.ajax({
        url: "/artist",
        type: "GET",
        success: function(data) {
            // 받아온 아티스트 목록을 표시
            const $artistList = $('#artistList .row');
            const artistTemplate = $('#artistCardTemplate').html();

            $.each(data, function(index, artist) {
                const filledTemplate = artistTemplate.replace(/{{artist.profileImg}}/g, artist.profileImg)
                    .replace(/{{artist.name}}/g, artist.name)
                    .replace(/{{artist.age}}/g, artist.age)
                    .replace(/{{artist.gender}}/g, artist.gender)
                    .replace(/{{artist.id}}/g, artist.id)
                    .replace(/{{artist.artistLike}}/g, artist.likes)
                    .replace(/{{buttonText}}/g, artist.isSubscribed ? '구독중' : '구독');

                // 가로로 3개씩 채우기
                if (index % 3 === 0) {
                    $artistList.append('<div class="w-100"></div>'); // 줄 바꾸기
                }
                $artistList.append(filledTemplate);

                const $artistCard = $('[data-artist-id="' + artist.id + '"]');
                const $likeImage = $artistCard.find('.like-btn');

                console.log("좋아요했나요...?", artist.isLiked);

                if (artist.isLiked) {
                    $likeImage.attr('src', 'images/like.png'); // 이미지 경로를 좋아요 이미지로 변경
                } else {
                    $likeImage.attr('src', 'images/unlike.png'); // 이미지 경로를 좋아요 해제 이미지로 변경
                }
            });
            // 좋아요 버튼 클릭 이벤트 처리
            $('.like-btn').click(function() {
                const artistId = $(this).data('artist-id');
                const $likeImage = $(this);

                if ($likeImage.attr('src')==='images/unlike.png'){
                    $likeImage.attr('src', 'images/like.png');
                }else {
                    $likeImage.attr('src', 'images/unlike.png');
                }

                // 여기에 좋아요 기능을 추가할 수 있습니다.
                console.log('좋아요 클릭 - 아티스트 ID:', artistId);
                $.ajax({
                    url: "/artist/"+artistId+"/like",
                    type: "POST",
                    contentType: "application/json",
                    success: function (data) {
                        //좋아요 수 업데이트
                        const updatedLikes = data.likes;
                        console.log(updatedLikes);

                        // 해당 아티스트 카드를 찾아서 좋아요 수 업데이트
                        const $artistCard = $likeImage.closest('.artist-card');
                        $artistCard.find('.likes-count').text(updatedLikes);
                    },
                    error: function (xhr, status, error) {
                        console.error("AJAX 요청 실패: ", status, error);
                    },
                })
            });

            // 아티스트 구독 버튼 클릭 이벤트 처리
            $('.artist-subscribe-btn').click(function () {
                const artistId = $(this).data('artist-id');
                const $subscribeBtn = $(this);

                const buttonText = $subscribeBtn.text();

                if (buttonText==='구독') {
                    $subscribeBtn.text('구독중');
                } else {
                    $subscribeBtn.text('구독');
                }
                console.log('아티스트 구독 클릭 - 장르 ID:', artistId);
                $.ajax({
                    url: "/artist/"+artistId+"/subscribe",
                    type: "POST",
                    contentType: "application/json",
                    success: function (data) {
                    },
                    error: function (xhr, status, error) {
                        console.error("AJAX 요청 실패: ", status, error);
                    },
                })
            });
        },
        error: function (xhr, status, error) {
            console.error("AJAX 요청 실패:", status, error);
        }
    });

    $.ajax({
        url: "/genre",
        type: "GET",
        success: function(data) {
            // 받아온 장르 목록을 표시
            const $genreList = $('#genreList .row');
            const genreTemplate = $('#genreCardTemplate').html();

            $.each(data, function(index, genre) {
                const filledTemplate = genreTemplate.replace(/{{genre.name}}/g, genre.name)
                    .replace(/{{genre.id}}/g, genre.id)
                    .replace(/{{buttonText}}/g, genre.isSubscribed ? '구독중' : '구독'); // 버튼 텍스트 설정

                $genreList.append('<div class = "w-100"></div>');

                $genreList.append(filledTemplate);
            });

            // 장르 구독 버튼 클릭 이벤트 처리
            $('.genre-subscribe-btn').click(function () {
                const genreId = $(this).data('genre-id');
                const $genreBtn = $(this);

                const buttonText = $genreBtn.text();

                if (buttonText==='구독') {
                    $genreBtn.text('구독중');
                } else {
                    $genreBtn.text('구독');
                }
                console.log('장르 구독 클릭 - 장르 ID:', genreId);
                $.ajax({
                    url: "/genre/"+genreId+"/subscribe",
                    type: "POST",
                    contentType: "application/json",
                    success: function (data) {
                    },
                    error: function (xhr, status, error) {
                        console.error("AJAX 요청 실패: ", status, error);
                    },
                })
            });
        },
        error: function (xhr, status, error) {
            console.error("AJAX 요청 실패:", status, error);
        }
    });
});
