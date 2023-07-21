import React from 'react';
import Header from '../../components/Header/Header';
import RankAvartar from '../../components/Avarta/RankAvartar/RackAvartar';
import ExpPointBar from '../../components/Element/ExpPointBar';
import './MyPage.css';

type SideBarProps = {
  points: number; // 현재 유저의 포인트
  percent: number; //경험치 바의 퍼센트 값 (0~100 사이의 숫자)
  match: number; // 지금까지 참여한 미팅 횟수
};

function SideBar({ points, percent, match }: SideBarProps) {
  return (
    <div className="side-bar">
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
      <div className="footer">
        <p>비밀번호 변경</p>
      </div>
    </div>
  );
}

function MyPage() {
  return (
    <div className="mypage">
      <Header />
      <main>
        <SideBar percent={75} points={2750} match={109} />
      </main>
    </div>
  );
}

export default MyPage;
