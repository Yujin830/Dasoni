import React, { useState } from 'react';
import titleImg from '../../assets/image/title_img.png';
import maleIcon from '../../assets/image/male_icon.png';
import femaleIcon from '../../assets/image/female_icon.png';
import FilledButton from '../Button/FilledButton';
import './RoomBox.css';
import axios, { AxiosError } from 'axios';
import { useAppSelector } from '../../app/hooks';
import { useNavigate } from 'react-router';
import { useDispatch } from 'react-redux';
import {
  setRatingLimit,
  setRoomTitle,
  setRoomType,
  setWaitingMemberList,
  setWaitingRoomId,
} from '../../app/slices/waitingSlice';

export type RoomBoxProps = {
  roomId: number; // room을 구분하는 id
  title: string; // 방 제목
  maleMemberCount: number; // 현재 남자 참가인원
  femaleMemberCount: number; // 현재 여자 참가인원
  maleAvgRating: number; // 참가한 남자 평균 레이팅
  femaleAvgRating: number; // 참가한 여자 평균 레이팅
  ratingLimit: number; // 랭크 제한
};

type GenderInfoProps = {
  genderIcon: string; // 성별 아이콘
  genderCount: number; // 성별  참여 인원
  genderAvgRank: number; // 성별 평균 등급
  fullCount: number; // 최대 참여 가능 인원수
};

const styles = {
  //FilledButton 컴포넌트 일반 style
  basic: {
    width: 'auto',
    height: '2rem',
    borderRadius: '6rem',
    background: '#EC5E98',
    color: '#FFF',
    textAlign: 'center',
    fontWize: '0.5rem',
    fontWeight: 500,
    padding: '0.5rem 0.8rem',
  },
  //Filledbutton 컴포넌트 메기 입장 style
  megi: {
    width: 'auto',
    height: '2rem',
    borderRadius: '6rem',
    background: '#ECC835',
    color: '#FFF',
    textAlign: 'center',
    fontWize: '0.5rem',
    fontWeight: 500,
    padding: '0.5rem 0.8rem',
  },
  //FilledButton 컴포넌트 disabled style
  disabled: {
    width: 'auto',
    height: '2rem',
    borderRadius: '6rem',
    background: '#8B8B8B',
    color: '#FFF',
    textAlign: 'center',
    fontWize: '0.5rem',
    fontWeight: 500,
    padding: '0.5rem 0.8rem',
  },
};

const FULL_COUNT = 3; // 최대 가능 인원수

// 성별 정보 컴포넌트
function GenderInfo({ genderIcon, genderCount, genderAvgRank, fullCount }: GenderInfoProps) {
  return (
    <div className="gender-info">
      <img className="item" src={genderIcon} alt="성별 아이콘" />
      <span className="count item">
        {genderCount}/{fullCount}
      </span>
      <span className="avg-rank item">평균 등급 {genderAvgRank}</span>
    </div>
  );
}

function RoomBox({
  roomId,
  title,
  maleMemberCount,
  femaleMemberCount,
  maleAvgRating,
  femaleAvgRating,
  ratingLimit,
}: RoomBoxProps) {
  const member = useAppSelector((state) => state.user);
  const navigate = useNavigate();
  const dispatch = useDispatch();

  // 입장하기
  const onClickEnter = async (event: React.MouseEvent<HTMLButtonElement>) => {
    event.preventDefault();
    if (member.remainLife === 0) {
      alert('오늘은 모든 라이프를 소진하여 더 이상 입장할 수 없습니다.');
      return;
    } else {
      // console.log('입장하기');
      try {
        const res = await axios.post(`/api/rooms/${roomId}/members/${member.memberId}`);
        // console.log(res);

        if (res.status === 200) {
          // console.log('입장 성공');
          dispatch(setRoomType('private'));
          dispatch(setWaitingRoomId(roomId));
          dispatch(setRoomTitle(title));
          dispatch(setRatingLimit(ratingLimit));
          navigate(`/waiting-room/${roomId}`);

          const waitingMember = {
            member: {
              memberId: member.memberId,
              nickname: member.nickname,
              gender: member.gender,
              profileImageSrc: member.profileImageSrc,
              rating: member.rating,
              meetingCount: member.matchCnt,
              job: member.job,
            },
            roomLeader: false,
            specialUser: false,
          };

          dispatch(setWaitingMemberList([waitingMember]));
        }
      } catch (err) {
        const error = err as AxiosError;
        if (error.response && error.response.status === 403) {
          alert('마이페이지에서 추가 정보를 먼저 입력해주세요!');
        } else {
          console.error('Error during room creation:', err);
          alert('방 생성 중 문제가 발생했습니다.');
        }
      }
    }
  };

  return (
    <div className="room-box">
      <div className="room-header">
        <div className="title-box">
          <img src={titleImg} alt="하트 이미지" />
          <h4>{title}</h4>
        </div>
        {femaleMemberCount + maleMemberCount < 2 * FULL_COUNT ? (
          <FilledButton style={styles.basic} content="입장하기" handleClick={onClickEnter} />
        ) : null}
      </div>
      <div className="content">
        <GenderInfo
          genderIcon={maleIcon}
          genderCount={maleMemberCount}
          genderAvgRank={maleAvgRating}
          fullCount={FULL_COUNT}
        />
        <GenderInfo
          genderIcon={femaleIcon}
          genderCount={femaleMemberCount}
          genderAvgRank={femaleAvgRating}
          fullCount={FULL_COUNT}
        />
      </div>
    </div>
  );
}

export default RoomBox;
