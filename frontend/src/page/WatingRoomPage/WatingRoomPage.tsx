import React, { useState, useCallback, useMemo } from 'react';
import Header from '../../components/Header/Header';
import titleLogo from '../../assets/image/title_img.png';
import { WaitingMember } from '../../apis/response/waitingRoomRes';
import WaitingMemberBox from '../../components/WatingMember/WaitingMember';
import FilledButton from '../../components/Button/FilledButton';
import { useNavigate, useParams } from 'react-router';
import './WaitingRoomPage.css';
import axios from 'axios';
import ChatRoom from '../../components/ChatRoom/ChatRoom';

import { useWebSocket } from '../../hooks/useWebSocket';
import { useAppSelector } from '../../app/hooks';
import convertScoreToName from '../../utils/convertScoreToName';

// const styles = {
//   startBtn: {
//     width: '10rem',
//     height: '4rem',
//     borderRadius: ' 0.5rem',
//     background: '#ECC835',
//     color: '#fff',
//     fontSize: '1.5rem',
//     fontWeight: '700',
//   },
//   exitBtn: {
//     width: '10rem',
//     height: '4rem',
//     borderRadius: ' 0.5rem',
//     background: '#EC5E98',
//     color: '#fff',
//     fontSize: '1.5rem',
//     fontWeight: '700',
//   },
// };

function WaitingRoomPage() {
  const waitingRoomInfo = useAppSelector((state) => state.waitingRoom);
  const { gender } = useAppSelector((state) => state.user);
  const [memberList, setMemberList] = useState<WaitingMember[]>(
    waitingRoomInfo.waitingRoomMemberList,
  );

  const sameGenderMemberList = useMemo(
    () => memberList.filter((member) => member.gender === gender),
    [memberList],
  );

  const diffGenderMemberList = useMemo(
    () => memberList.filter((member) => member.gender !== gender),
    [memberList],
  );
  const navigate = useNavigate();
  const { roomId } = useParams();
  const member = useAppSelector((state) => state.user);

  // webSocket 사용해 실시간으로 대기방에 입장하는 memberList 갱신
  const client = useWebSocket({
    subscribe: (client) => {
      client.subscribe(`/topic/room/${roomId}`, (res: any) => {
        // console.log(res.body);
        console.log(JSON.parse(res.body));
        const data = JSON.parse(res.body);
        console.log('received data: ', data);
        setMemberList(data.memberList);
      });

      client.subscribe(`/topic/room/${roomId}/start`, (res: any) => {
        console.log('게임 시작 메세지 전송');
        console.log(res.body);

        if (res.body === 'Start') {
          setTimeout(() => {
            navigate(`/meeting/${roomId}`, { replace: true });
          }, 3000);
        }
      });
      // 서버가 받을 주소(string), 헤더({[key: string]}: any;|undefined), 전달할 메세지(string|undefined)
      const joinData = {
        type: 'join',
        memberId: member.memberId,
      };
      client.send(`/app/room/${roomId}`, {}, JSON.stringify(joinData));
    },
  });

  const handleStartBtn = () => {
    alert('미팅이 3초 후 시작됩니다');
    client?.send(`/app/room/${roomId}/start`);
  };

  const handleExitBtn = async () => {
    console.log('방 나가기');
    try {
      console.log('roomID ', roomId);
      const res = await axios.delete(`/api/rooms/${roomId}/members/${member.memberId}`);
      console.log(res);

      const data = {
        type: 'quit',
        memeberId: member.memberId,
      };
      client?.send(`/app/room/${roomId}`, {}, JSON.stringify(data));
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
      <Header onModalToggle={handleModalToggle} />
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
        <div id="waiting-room-body">
          <div id="member-list-box">
            <div className="waiting-room-content">
              {sameGenderMemberList.map((member) => (
                <WaitingMemberBox
                  key={member.memberId}
                  nickname={member.nickname}
                  rating={member.rating}
                  matchCnt={member.meetingCount}
                  gender={member.gender}
                  profileImageSrc={member.profileImageSrc}
                />
              ))}
            </div>
            <div className="waiting-room-content">
              {diffGenderMemberList.map((member) => (
                <WaitingMemberBox
                  key={member.memberId}
                  nickname={member.nickname}
                  rating={member.rating}
                  matchCnt={member.meetingCount}
                  gender={member.gender}
                  profileImageSrc={member.profileImageSrc}
                />
              ))}
            </div>
          </div>
          <ChatRoom />
        </div>
        <div id="waiting-room-footer">
          <FilledButton content="시작하기" classes="btn start-btn" handleClick={handleStartBtn} />
          <FilledButton content="나가기" classes="btn exit-btn" handleClick={handleExitBtn} />
        </div>
      </main>
    </div>
  );
}

export default WaitingRoomPage;
