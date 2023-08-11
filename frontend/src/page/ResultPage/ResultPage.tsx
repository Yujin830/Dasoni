import React, { useEffect, useState } from 'react';
import Header from '../../components/Header/Header';
import Banner from '../../components/Banner/Banner';
import axios from 'axios';
import { useAppSelector } from '../../app/hooks';
import FailResult from '../../components/Result/FailResult/FailResult';
import SuccessResult from '../../components/Result/SuccessResult/SuccessResult';
import FilledButton from '../../components/Button/FilledButton';
import { useNavigate } from 'react-router';

function ResultPage() {
  const [success, setSuccess] = useState(false);
  const { memberId, profileImageSrc, rating } = useAppSelector((state) => state.user);
  const { resultOfRoomMember } = useAppSelector((state) => state.meetingRoom);
  const [otherProfile, setOtherProfile] = useState('');

  const navigate = useNavigate();

  useEffect(() => {
    setResult();
  }, []);

  const setResult = async () => {
    if (resultOfRoomMember.matchMemberId !== 0) {
      setSuccess(true);

      // TODO : 매칭된 상대방 정보 가져오기
    }
  };

  const handleClose = () => {
    console.log('메인으로');
    navigate('/main', { replace: true });
  };
  return (
    <div id="result">
      <Header />
      <Banner />
      <main>
        <div id="title-box">
          <h2>매칭 결과</h2>
          <h3>{success ? '매칭에 성공했습니다!' : '매칭에 실패했습니다...'}</h3>
        </div>
        <div id="user-info-box">
          {success ? (
            // TODO : 상대방 정보 불러오기
            <SuccessResult
              profileSrc={profileImageSrc}
              rating={rating}
              otherProfileSrc={profileImageSrc}
              otherRating={rating}
            />
          ) : (
            <FailResult profileSrc={profileImageSrc} />
          )}
        </div>
        <h3 className="result-rating">{resultOfRoomMember.ratingChange}point</h3>
        <div className="buttons">
          {success && <button>연장하기</button>}
          <FilledButton content="메인으로" classes="to-main-btn" handleClick={handleClose} />
        </div>
      </main>
    </div>
  );
}

export default ResultPage;
