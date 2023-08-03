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
  const [memberList, setMemberList] = useState<WaitingMember[]>([]);
  const navigate = useNavigate();
  const { roomId } = useParams();
  const waitingRoomInfo = useAppSelector((state) => state.waitingRoom);

  // webSocket 사용해 실시간으로 대기방에 입장하는 memberList 갱신
  useWebSocket({
    subscribe: (client) => {
      client.subscribe(`/topic/room/${roomId}`, (res: any) => {
        console.log(res);
        console.log(JSON.parse(res.body));
        // const result = JSON.parse(res.body);
        setMemberList(JSON.parse(res.body));
      });

      // 이 부분을 수정하였습니다. 두 번째 인자로 ChatMessage 객체가 필요합니다.
      // 단, 실제 사용 시에는 적절한 필드값을 설정해야 합니다.
      const chatMessage = {
        sender: 'username', // sender는 실제 사용자의 이름이어야 합니다.
        content: 'Hello, world!', // content는 실제 메시지 내용이어야 합니다.
      };
      // 서버가 받을 주소(string), 헤더({[key: string]}: any;|undefined), 전달할 메세지(string|undefined)
      client.send(`/app/room/${roomId}`, {}, JSON.stringify(chatMessage));
    },
    beforeDisconnected: (client) => {
      console.log(client);
      setMemberList([]);
    },
  });

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
