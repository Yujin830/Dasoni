import React, { useState, useCallback } from 'react';
import Header from '../../components/Header/Header';
import titleLogo from '../../assets/image/title_img.png';
import { WaitingMember } from '../../apis/response/waitingRoomRes';
import WaitingMemberBox from '../../components/WatingMember/WaitingMember';
import FilledButton from '../../components/Button/FilledButton';
import { useNavigate, useParams } from 'react-router';
import './WaitingRoomPage.css';
import axios from 'axios';
import { useWebSocket } from '../../hooks/useWebSocket';
import { useAppSelector } from '../../app/hooks';
import convertScoreToName from '../../utils/convertScoreToName';

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
  const waitingRoomInfo = useAppSelector((state) => state.waitingRoom);

  // webSocket 사용해 실시간으로 대기방에 입장하는 memberList 갱신
  useWebSocket({
    subscribe: (client) => {
      client.subscribe(`/topic/room/${roomId}`, (res) => {
        console.log(res);
        console.log(JSON.parse(res.body));
        // const result = JSON.parse(res.body);
        // setMemberList(result.members);
      });

      // 이 부분을 수정하였습니다. 두 번째 인자로 ChatMessage 객체가 필요합니다.
      // 단, 실제 사용 시에는 적절한 필드값을 설정해야 합니다.
      const chatMessage = {
        sender: 'username', // sender는 실제 사용자의 이름이어야 합니다.
        content: 'Hello, world!', // content는 실제 메시지 내용이어야 합니다.
      };
      client.publish({ destination: `/app/room/${roomId}`, body: JSON.stringify(chatMessage) });
    },
    beforeDisconnected: (client) => {
      console.log(client);
      setMemberList([]);
    },
  });
  // useWebSocket({
  //   subscribe: (client) => {
  //     // 정의한 주소로 서버로부터 메세지 구독 :: 해당 주소로 서버에서 메세지가 전송되면
  //     // 클라이언트에서 해당 메세지 받아와 처리
  //     // 구독할 주소 : 백엔드에 정의된 주소
  //     client.subscribe(`/topic/room/${roomId}`, (res) => {
  //       console.log('res', res); // 서버에서 보내온 메세지
  //       const result = JSON.parse(res.body);
  //       setMemberList(result.members);
  //     });

  //     // 정의한 주소로 메세지를 서버로 전송,
  //     // 서버에서 해당 주소를 구독한 클라이언트들이 메세지 수신함
  //     client.send(`/app/room/${roomId}`);
  //   },
  //   beforeDisconnected: (client) => {
  //     console.log(client);
  //     setMemberList([]);
  //   },
  // });

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
            {waitingRoomInfo.roomTitle}
          </div>
          <div className="info">
            <span>메기 : {waitingRoomInfo.megiAcceptable ? 'Yes' : 'No'}</span>
            <span>
              Rank :
              {convertScoreToName(waitingRoomInfo.ratingLimit ? waitingRoomInfo.ratingLimit : 0)}
            </span>
          </div>
        </div>
        <div id="waiting-room-content">
          {memberList.map((member) => (
            <WaitingMemberBox key={member.memberId} />
          ))}
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
