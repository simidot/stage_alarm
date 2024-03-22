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
                    .replace(/{{artist.artistLike}}/g, artist.likes);

                // 가로로 3개씩 채우기
                if (index % 3 === 0) {
                    $artistList.append('<div class="w-100"></div>'); // 줄 바꾸기
                }
                $artistList.append(filledTemplate);

                const $artistCard = $('[data-artist-id="' + artist.id + '"]');
                const $likeImage = $artistCard.find('.like-btn');


                // 좋아요 상태에 따라 이미지 업데이트
                // const $likeBtn = $('[data-artist-id="' + artist.id + '"] .like-btn');
                // const $likeImage = $likeBtn.find('img');
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

                        // 해당 아티스트 카드를 찾아서 좋아요 수 없데이트
                        const $artistCard = $likeImage.closest('.artist-card');
                        $artistCard.find('.likes-count').text(updatedLikes);
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
            // 받아온 아티스트 목록을 표시
            const $genreList = $('#genreList .row');
            const genreTemplate = $('#genreCardTemplate').html();

            $.each(data, function(index, genre) {
                const filledTemplate = genreTemplate.replace(/{{genre.name}}/g, genre.name)
                    .replace(/{{genre.id}}/g, genre.id);
                $genreList.append('<div class = "w-100"></div>');

                $genreList.append(filledTemplate);
            });
        },
        error: function (xhr, status, error) {
            console.error("AJAX 요청 실패:", status, error);
        }
    });
    // 장르 구독 버튼 클릭 이벤트 처리
    $(document).on('click', '.subscribe-btn', function () {
        const genreId = $(this).data('id');
        $.ajax({
            url: "/genre"
        })
        // 여기에 장르 구독 기능을 추가할 수 있습니다.
        console.log('장르 구독 클릭 - 장르 ID:', genreId);
    });
});
