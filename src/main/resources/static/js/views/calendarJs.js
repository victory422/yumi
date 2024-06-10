/**
 * Script info
 * 1. file path		: /static/js/views/codeDetailJs.js
 * 2. project name	: yumi korea Admin
 * 3. description	: 공통코드 관리 스크립트 
 */

// 현재 날짜 정보
const today = new Date();
const currentYear = today.getFullYear();
const currentMonth = today.getMonth();

/* 화면 로드 후 이벤트 */
window.addEventListener("load", function() {
	// 초기 달력 표시
	updateCalendar(new Date(currentYear, currentMonth, 1));
	
	// 이전 달 버튼 클릭 이벤트
	const prevButton = document.querySelector('.calendar-button.prev');
	prevButton.addEventListener('click', () => {
		const currentYear = new Date().getFullYear();
		const currentMonth = new Date().getMonth() - 1; // 0부터 시작하기 때문에 -1 해줍니다.
		const prevDate = new Date(currentYear, currentMonth, 1);
		updateCalendar(prevDate);
	});
	
	// 다음 달 버튼 클릭 이벤트
	const nextButton = document.querySelector('.calendar-button.next');
	nextButton.addEventListener('click', () => {
		const currentYear = new Date().getFullYear();
		const currentMonth = new Date().getMonth() + 1; // 0부터 시작하기 때문에 +1 해줍니다.
		const nextDate = new Date(currentYear, currentMonth, 1);
		updateCalendar(nextDate);
	});
});



// 달력 표시 함수
function updateCalendar(date) {
	const year = date.getFullYear();
	const month = date.getMonth();

	// 요일 정보
	const firstDay = new Date(year, month, 1);
	const dayOfWeek = firstDay.getDay(); // 0: 일요일, 1: 월요일, ... 6: 토요일

	// 달력 날짜 표시
	const calendarDays = document.getElementById('calendar-days');
	calendarDays.innerHTML = ''; // 기존 내용 지우기

	// 빈 날짜 채우기 (이전 달)
	for (let i = 0; i < dayOfWeek; i++) {
		const prevDate = new Date(year, month - 1, i + 1);
		const prevDayElement = document.createElement('div');
		prevDayElement.classList.add('calendar-day', 'prev-day');
		prevDayElement.textContent = prevDate.getDate();
		calendarDays.appendChild(prevDayElement);
	}

	// 현재 달 날짜 표시
	const numberOfDays = new Date(year, month + 1, 0).getDate(); // 마지막 날짜
	for (let i = 1; i <= numberOfDays; i++) {
		const currentDate = new Date(year, month, i);
		const currentDayElement = document.createElement('div');
		currentDayElement.classList.add('calendar-day');

		// 오늘 날짜 표시
		if (currentDate.getFullYear() === currentYear && currentDate.getMonth() === currentMonth && currentDate.getDate() === today.getDate()) {
			currentDayElement.classList.add('today');
		}

		currentDayElement.textContent = currentDate.getDate();
		calendarDays.appendChild(currentDayElement);
	}

	// 다음 달 빈 날짜 채우기
	const lastDayOfMonth = new Date(year, month, numberOfDays);
	const lastDayOfWeek = lastDayOfMonth.getDay(); // 0: 일요일, 1: 월요일, ... 6: 토요일
	for (let i = 1; i <= 7 - lastDayOfWeek; i++) {
		const nextDate = new Date(year, month + 1, i);
		const nextDayElement = document.createElement('div');
		nextDayElement.classList.add('calendar-day', 'next-day');
		nextDayElement.textContent = nextDate.getDate();
		calendarDays.appendChild(nextDayElement);
	}

	// 달력 제목 업데이트
	const calendarTitle = document.querySelector('.calendar-title');
	calendarTitle.textContent = `${year}년 ${month + 1}월`;
}