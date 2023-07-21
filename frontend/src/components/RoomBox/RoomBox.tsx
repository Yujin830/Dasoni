import React, { useState } from 'react';
import titleImg from '../../assets/image/title_img.png';
import maleIcon from '../../assets/image/male_icon.png';
import femaleIcon from '../../assets/image/female_icon.png';
import FilledButton from '../Button/FilledButton';
import './RoomBox.css';

type RoomBoxProps = {
  title: string; // 방 제목
  maleCnt: number; // 현재 남자 참가인원
  femaleCnt: number; // 현재 여자 참가인원
  maleAvgRank: string; // 참가한 남자 평균 랭크
  femaleAvgRank: string; // 참가한 여자 평균 랭크
  isMegiOpen: boolean; // 메기 입장 가능 여부
};

type GenderInfoProps = {
  genderIcon: string; // 성별 아이콘
  genderCount: number; // 성별  참여 인원
  genderAvgRank: string; // 성별 평균 등급
  fullCount: number; // 최대 참여 가능 인원수
};

const styles = {
  //FilledButton 컴포넌트 일반 style
  basic: {
    height: '2rem',
    borderRadius: '6rem',
    background: '#EC5E98',
    color: '#FFF',
    textAlign: 'center',
    fontWize: '0.5rem',
    fontWeight: 500,
    padding: '0.5rem 0.8rem',
  },
  //FilledButton 컴포넌트 disabled style
  disabled: {
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
const MEGI_FULL_COUNT = 4; // 메가 참여 가능 방 최대 가능 인원수

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
  title,
  maleCnt,
  femaleCnt,
  maleAvgRank,
  femaleAvgRank,
  isMegiOpen,
}: RoomBoxProps) {
  const [isFull, setIsFull] = useState(false); // 참여 인원이 가득 찼는지 저장하는 state
  // TODO : isFull 확인하는 로직

  // 입장하기 버튼 클릭 시 동작하는 함수
  const handleEnter = (event: React.MouseEvent<HTMLButtonElement>) => {
    event.preventDefault();
    if (isFull) alert('더 이상 입장 할 수 없습니다.');
    else console.log('입장하기');
  };

  return (
    <div className={isFull ? 'room-box disabled' : 'room-box'}>
      <div className="header">
        <div className="title-box">
          <img src={titleImg} alt="하트 이미지" />
          <h4>{title}</h4>
        </div>
        <FilledButton
          style={isFull ? styles.disabled : styles.basic}
          content="입장하기"
          handleClick={handleEnter}
        />
      </div>
      <div className="content">
        <GenderInfo
          genderIcon={maleIcon}
          genderCount={maleCnt}
          genderAvgRank={maleAvgRank}
          fullCount={isMegiOpen ? MEGI_FULL_COUNT : FULL_COUNT}
        />
        <GenderInfo
          genderIcon={femaleIcon}
          genderCount={femaleCnt}
          genderAvgRank={femaleAvgRank}
          fullCount={isMegiOpen ? MEGI_FULL_COUNT : FULL_COUNT}
        />
      </div>
    </div>
  );
}

export default RoomBox;
