import React, { useState, useCallback } from 'react';
import Header from '../../components/Header/Header';
import titleLogo from '../../assets/image/title_img.png';
import { WaitingMember } from '../../apis/response/waitingRoomRes';
import WaitingMemberBox from '../../components/WatingMember/WaitingMember';
import FilledButton from '../../components/Button/FilledButton';
import { useNavigate, useParams } from 'react-router';
import './WaitingRoomPage.css';
import axios from 'axios';
import ChatRoom from '../../components/ChatRoom/ChatRoom';

const watingMember: WaitingMember[] = [
  {
    memberId: 1,
    nickname: 'strong',
    profileSrc: 'rank_profile.png',
    gender: 'male',
    matchCnt: 56,
    points: 1800,
  },
  {
    memberId: 2,
    nickname: 'strong2',
    profileSrc: 'rank_profile.png',
    gender: 'male',
    matchCnt: 56,
    points: 1800,
  },
  {
    memberId: 3,
    nickname: 'strong3',
    profileSrc: 'rank_profile.png',
    gender: 'male',
    matchCnt: 56,
    points: 1800,
  },
  {
    memberId: 4,
    nickname: 'strong',
    profileSrc: 'rank_profile.png',
    gender: 'female',
    matchCnt: 56,
    points: 1800,
  },
  {
    memberId: 5,
    nickname: 'strong',
    profileSrc: 'rank_profile.png',
    gender: 'female',
    matchCnt: 56,
    points: 1800,
  },
  {
    memberId: 6,
    nickname: 'strong',
    profileSrc: 'rank_profile.png',
    gender: 'female',
    matchCnt: 56,
    points: 1800,
  },
];

const styles = {
  startBtn: {
    width: '10rem',
    height: '4rem',
    borderRadius: ' 0.5rem',
    background: '#ECC835',
    color: '#fff',
    fontSize: '1.5rem',
    fontWeight: '700',
  },
  exitBtn: {
    width: '10rem',
    height: '4rem',
    borderRadius: ' 0.5rem',
    background: '#EC5E98',
    color: '#fff',
    fontSize: '1.5rem',
    fontWeight: '700',
  },
};

function WaitingRoomPage() {
  const [memberList, setMemberList] = useState<WaitingMember[]>(watingMember);
  const navigate = useNavigate();
  const { roomId } = useParams();

  const handleStartBtn = () => {
    alert('미팅이 3초 후 시작됩니다');
    setTimeout(() => {
      navigate(`/meeting/${roomId}`);
    }, 3000);
  };

  const handleExitBtn = async () => {
    console.log('방 나가기');
    try {
      console.log('roomID ', roomId);
      const res = await axios.delete(`/rooms/${roomId}/members/1`);
      console.log(res);

      if (res.status === 200) {
        navigate('/main', { replace: true });
      }
    } catch (err) {
      console.error(err);
    }
  };
  // 모달 띄우기
  const [modalVisible, setModalVisible] = useState(false);

  const handleModalToggle = useCallback(() => {
    setModalVisible((prev) => !prev);
  }, []);

  return (
    <div id="waiting-page">
      <Header onModalToggle={handleModalToggle} />{' '}
      <main id="waiting-room-box">
        <div id="waiting-room-top">
          <div className="title">
            <img src={titleLogo} alt="하트 이미지" />
            재밌게 놀아요
          </div>
          <div className="info">
            <span>메기 : No</span>
            <span>Rank : Yellow</span>
          </div>
        </div>
        <div id="waiting-room-body">
          <div id="waiting-room-content">
            {memberList.map((member) => (
              <WaitingMemberBox key={member.memberId} />
            ))}
          </div>
          <ChatRoom />
        </div>
        <div id="waiting-room-footer">
          <FilledButton content="시작하기" style={styles.startBtn} handleClick={handleStartBtn} />
          <FilledButton content="나가기" style={styles.exitBtn} handleClick={handleExitBtn} />
        </div>
      </main>
    </div>
  );
}

export default WaitingRoomPage;
