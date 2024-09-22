// 장르 버튼
const genres = ["액션", "미스터리", "스릴러", "SF", "코메디", "드라마", "어드벤처", "범죄", "가족", "뮤지컬", "로맨스", "멜로", "뮤직", "자연", "인물", "다큐멘터리", "애니메이션", "판타지", "호러"];
const genreButtonsDiv = document.getElementById('genre-buttons');

// 버튼 항목에 추가
genres.forEach(genre => {
    const button = document.createElement('button');
    button.className = 'genre-button';
    button.textContent = genre;
    button.onclick = () => redirectToGenrePage(genre);
    genreButtonsDiv.appendChild(button);
});

function redirectToGenrePage(genre) {
    window.location.href = `/cinema/home?genreName=${encodeURIComponent(genre)}`;
}

// 화살표 버튼 스크롤
document.querySelector('.arrow-left').addEventListener('click', () => {
    document.querySelector('#genre-buttons').scrollBy(-200, 0);
});

document.querySelector('.arrow-right').addEventListener('click', () => {
    document.querySelector('#genre-buttons').scrollBy(200, 0);
});