import React, { useState, useEffect } from 'react';
import Header from '../../components/Header/Header';
import RankAvartar from '../../components/Avarta/RankAvartar/RackAvartar';
import ExpPointBar from '../../components/Element/ExpPointBar';
import './MyPage.css';
import MyProfile from '../../components/MyPage/MyProfile';
import MyProfileModify from '../../components/MyPage/Modify/MyProfileModify';
import MyProfileChangePw from '../../components/MyPage/ChangePassword/MyProfileChangePw';
import { useAppSelector } from '../../app/hooks';

type SideBarProps = {
  points: number; // 현재 유저의 포인트
  percent: number; //경험치 바의 퍼센트 값 (0~100 사이의 숫자)
  match: number; // 지금까지 참여한 미팅 횟수
  setType: (type: string) => void; //set state 함수
};

function SideBar({ percent, match, points, setType }: SideBarProps) {
  const { rating, matchCnt } = useAppSelector((state) => state.user);
  const changePw = (e: React.MouseEvent<HTMLAnchorElement>) => {
    e.preventDefault();
    setType('changePw');
  };

  return (
    <div className="my-side-bar">
      <div className="top">
        <RankAvartar src="rank_profile.png" point={rating} />
        <h3>나전문</h3>
      </div>
      <div className="info">
        <div className="signal">
          <p className="title"> Signal</p>
          <ExpPointBar percent={percent} points={rating} />
        </div>
        <div className="match">
          <p className="title">Match</p>
          <h3>{matchCnt}</h3>
        </div>
      </div>
      <footer className="footer">
        <a href="/" onClick={changePw}>
          비밀번호 변경
        </a>
      </footer>
    </div>
  );
}

function MyPage() {
  const [type, setType] = useState('read');

  const [modalVisible, setModalVisible] = useState(false);

  const handleModalToggle = () => {
    setModalVisible(!modalVisible);
  };

  let content = null;
  if (type === 'read') content = <MyProfile setType={setType} />;
  else if (type === 'modify') content = <MyProfileModify setType={setType} />;
  else if (type === 'changePw') content = <MyProfileChangePw setType={setType} />;
  return (
    <div className="mypage">
      <Header onModalToggle={handleModalToggle} />
      <main>
        <SideBar percent={75} points={2750} match={109} setType={setType} />
        {content}
      </main>
    </div>
  );
}

export default MyPage;
