import React, { useEffect, useState } from 'react';
import Header from '../../components/Header/Header';
import Banner from '../../components/Banner/Banner';
import axios from 'axios';
import { useAppSelector } from '../../app/hooks';
import FailResult from '../../components/Result/FailResult/FailResult';
import SuccessResult from '../../components/Result/SuccessResult/SuccessResult';
import FilledButton from '../../components/Button/FilledButton';
import { useNavigate } from 'react-router';
import './ResultPage.css';
import { matchMemberInfo } from '../../apis/response/meetingRoomRes';
import {
  setFinalSignalReceiver,
  setMatchMemberId,
  setRatingChange,
} from '../../app/slices/meetingSlice';
import { useDispatch } from 'react-redux';

function ResultPage() {
  const [success, setSuccess] = useState(false);
  const { profileImageSrc, rating } = useAppSelector((state) => state.user);
  const { resultOfRoomMember } = useAppSelector((state) => state.meetingRoom);
  const [matchMember, setMatchMember] = useState<matchMemberInfo>({
    memberId: 0,
    profileImageSrc: 'null',
    rating: 1000,
  });

  const navigate = useNavigate();
  const dispatch = useDispatch();

  useEffect(() => {
    setResult();
  }, []);

  const setResult = async () => {
    // 매칭 성공 / 실패 확인
    if (resultOfRoomMember.matchMemberId !== 0) {
      setSuccess(true);

      // 정보 초기화
      dispatch(setFinalSignalReceiver(0)); // 선택한 최종투표자 초기화
      dispatch(setRatingChange(0)); // 변화한 레이팅 점수 초기화
      dispatch(setMatchMemberId(0)); // 매칭된 상대 memberId 초기화

      if (resultOfRoomMember.matchMemberId !== 0) {
        setSuccess(true);

        // 매칭된 상대방 정보 가져오기
        try {
          const res = await axios.get(`/api/users/${resultOfRoomMember.matchMemberId}`);
          console.log(res.data);

          if (res.status === 200) {
            setMatchMember(res.data);
          }
        } catch (err) {
          console.error(err);
        }
      }
    }
  };

  const handleClose = () => {
    console.log('메인으로');
    navigate('/main', { replace: true });
  };
  return (
    <div id="result-page">
      <Header />
      <Banner />
      <main>
        <div id="title-box">
          <h2 className="title">매칭 결과</h2>
          <h3 className="title-msg">
            {success ? '매칭에 성공했습니다!' : '매칭에 실패했습니다...'}
          </h3>
        </div>
        <div id="user-info-box">
          {success ? (
            <SuccessResult
              profileSrc={profileImageSrc}
              rating={rating}
              otherProfileSrc={matchMember.profileImageSrc}
              otherRating={matchMember.rating}
            />
          ) : (
            <FailResult profileSrc={profileImageSrc} />
          )}
        </div>
        <h3 className="result-rating">{resultOfRoomMember.ratingChange} point</h3>
        <div className="buttons">
          <FilledButton content="메인으로" classes="to-main-btn" handleClick={handleClose} />
        </div>
      </main>
    </div>
  );
}

export default ResultPage;
