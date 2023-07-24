import React, { useState, useEffect } from 'react';
import Header from '../../components/Header/Header';
import RankAvartar from '../../components/Avarta/RankAvartar/RackAvartar';
import ExpPointBar from '../../components/Element/ExpPointBar';
import './MyPage.css';
import { useAppSelector } from '../../app/hooks';
import RecentMatchAvartar from '../../components/Avarta/RecentMatchAvartar/RecentMatchAvartar';

type SideBarProps = {
  points: number; // 현재 유저의 포인트
  percent: number; //경험치 바의 퍼센트 값 (0~100 사이의 숫자)
  match: number; // 지금까지 참여한 미팅 횟수
};

function SideBar({ points, percent, match }: SideBarProps) {
  return (
    <div className="my-side-bar">
      <div className="top">
        <RankAvartar src="rank_profile.png" point={points} />
        <h3>나전문</h3>
      </div>
      <div className="info">
        <div className="signal">
          <p className="title"> Signal</p>
          <ExpPointBar percent={percent} points={points} />
        </div>
        <div className="match">
          <p className="title">Match</p>
          <h3>{match}</h3>
        </div>
      </div>
      <footer className="footer">
        <p>비밀번호 변경</p>
      </footer>
    </div>
  );
}

function MyPageContent() {
  const { id, nickname, job, birth, sido, gugun } = useAppSelector((state) => state.user);

  // TODO : 최근 매칭된 다소니 리스트 조회 recentUserList로 state 변경
  const recentUserList = useState([]);
  const faketUserList = [
    { profileImg: 'rank_profile.png', userId: 1 },
    { profileImg: 'rank_profile.png', userId: 2 },
    { profileImg: 'rank_profile.png', userId: 3 },
    { profileImg: 'rank_profile.png', userId: 4 },
    { profileImg: 'rank_profile.png', userId: 5 },
    { profileImg: 'rank_profile.png', userId: 6 },
  ];

  const deleteUser = (e: React.MouseEvent<HTMLAnchorElement>) => {
    e.preventDefault();
    if (confirm(`탈퇴하면 복구할 수 없습니다.\n정말 탈퇴하시겠습니까?`)) {
      // TODO : 회원 탈퇴 API 개발
      alert(`탈퇴 되었습니다.`);
      // location : 로그인 화면으로 이동
    }
  };

  useEffect(() => {
    // TODO : 최근 매칭된 다소니 리스트 조회 API 개발
  }, []);

  return (
    <div className="content">
      <p id="user">
        <span id="name">
          {nickname}({id})
        </span>
        님의 개인정보
      </p>
      <table>
        <tr>
          <td className="name">생년월일</td>
          <td className="info">{birth}</td>
        </tr>
        <tr>
          <td className="name">주소</td>
          <td className="info">
            {sido} {gugun}
          </td>
        </tr>
        <tr>
          <td className="name">직업</td>
          <td className="info">{job}</td>
        </tr>
      </table>
      <div id="recent-matched-user-box">
        <p>최근 매칭된 다소니</p>
        <div id="matched-user-list">
          {recentUserList.length > 0 ? (
            faketUserList.map((user) => (
              <RecentMatchAvartar key={user.userId} src={user.profileImg} />
            ))
          ) : (
            <p>최근 매칭된 다소니가 없습니다</p>
          )}
        </div>
      </div>
      <footer>
        <a className="btn modify" href="/">
          회원 정보 수정
        </a>
        <a className="btn remove" href="/" onClick={deleteUser}>
          회원 탈퇴
        </a>
      </footer>
    </div>
  );
}

function MyPage() {
  return (
    <div className="mypage">
      <Header />
      <main>
        <SideBar percent={75} points={2750} match={109} />
        <MyPageContent />
      </main>
    </div>
  );
}

export default MyPage;
