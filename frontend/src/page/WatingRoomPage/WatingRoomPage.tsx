import React, { useEffect, useState, useCallback, useMemo, useRef } from 'react';
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
// BGM
import song from '../../assets/music/lobby.mp3';
import AudioController from '../../components/AudioController/AudioController';

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

  const client = useWebSocket({
    subscribe: (client) => {
      client.subscribe(`/topic/room/${roomId}`, (res: any) => {
        const data = JSON.parse(res.body);
        setMemberList(data.memberList);
      });

      client.subscribe(`/topic/room/${roomId}/start`, (res: any) => {
        if (res.body === 'Start') {
          setTimeout(() => {
            navigate(`/meeting/${roomId}`, { replace: true });
          }, 3000);
        }
      });

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
    try {
      const res = await axios.delete(`/api/rooms/${roomId}/members/${member.memberId}`);

      const data = {
        type: 'quit',
        memberId: member.memberId,
      };
      client?.send(`/app/room/${roomId}`, {}, JSON.stringify(data));
      if (res.status === 200) {
        navigate('/main', { replace: true });
      }
    } catch (err) {
      console.error(err);
    }
  };

  const [modalVisible, setModalVisible] = useState(false);

  const handleModalToggle = useCallback(() => {
    setModalVisible((prev) => !prev);
  }, []);

  // Volume and Mute Controls
  const [volume, setVolume] = useState(1);
  const [muted, setMuted] = useState(false);

  const handleVolumeChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setVolume(Number(e.target.value));
  };

  const handleMuteToggle = () => {
    setMuted((prevMuted) => !prevMuted);
  };

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
          <AudioController
            volume={volume}
            muted={muted}
            songName="lobby"
            handleMuteToggle={handleMuteToggle}
            handleVolumeChange={handleVolumeChange}
          />
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
