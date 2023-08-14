import React, { useEffect, useState, useCallback, useMemo, useRef } from 'react';
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
import { useDispatch } from 'react-redux';
import { setWaitingMemberList } from '../../app/slices/waitingSlice';

function WaitingRoomPage() {
  const [isLoading, setIsLoading] = useState(true); // 로딩 상태를 관리하는 상태 변수

  const waitingRoomInfo = useAppSelector((state) => state.waitingRoom);
  const { gender } = useAppSelector((state) => state.user);
  const [memberList, setMemberList] = useState<WaitingMember[]>(
    waitingRoomInfo.waitingRoomMemberList,
  );

  const sameGenderMemberList = useMemo(
    () => memberList.filter((info) => info.member.gender === gender),
    [memberList],
  );

  const diffGenderMemberList = useMemo(
    () => memberList.filter((info) => info.member.gender !== gender),
    [memberList],
  );
  const navigate = useNavigate();
  const dispatch = useDispatch();
  const { roomId } = useParams();
  const member = useAppSelector((state) => state.user);
  const { waitingRoomMemberList } = useAppSelector((state) => state.waitingRoom);

  const client = useWebSocket({
    subscribe: (client) => {
      client.subscribe(`/topic/room/${roomId}`, (res: any) => {
        const data = JSON.parse(res.body);
        console.log(data);
        data.privateRoomInfo.roomMemberInfoList.map((roomMemberInfo: any) => {
          if (roomMemberInfo.member.memberId === member.memberId) {
            dispatch(setWaitingMemberList([roomMemberInfo]));
          }
        });
        setMemberList(data.privateRoomInfo.roomMemberInfoList);
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

  const handleStartBtn = async () => {
    // 미팅방 시작 시 목숨 감소, 매치 수 증가
    if (waitingRoomMemberList[0].roomLeader) {
      const res = await axios.patch(`/api/rooms/${roomId}`, {
        roomLeaderId: waitingRoomMemberList[0].roomMemberId,
        roomId: roomId,
      });
      console.log(res.data);

      if (res.data.status.code !== 1224) {
        alert('미팅이 3초 후 시작됩니다');
        client?.send(`/app/room/${roomId}/start`);
      }
    }
  };

  const handleExitBtn = async () => {
    try {
      const res = await axios.delete(`/api/rooms/${roomId}/members/${member.memberId}`);

      const quitData = {
        type: 'quit',
        memberId: member.memberId,
      };
      client?.send(`/app/room/${roomId}`, {}, JSON.stringify(quitData));
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

  const currentUserId = member.memberId;
  // memberList에서 방장(memberList에서 roomLeader가 true인)인지 여부를 확인하는 함수
  const isRoomLeaderInMemberList = (info: any) => {
    console.log(info);
    return info.roomLeader && info.member.memberId === currentUserId;
  };

  return (
    <div id="waiting-page">
      <main id="waiting-room-box">
        <div id="waiting-room-top">
          <div className="title">
            <img src={titleLogo} alt="하트 이미지" />
            {waitingRoomInfo.roomTitle}
          </div>
          <div className="info">
            <span>메기 : {waitingRoomInfo.megiAcceptable ? 'Yes' : 'No'}</span>
            <span>Rank :{convertScoreToName(waitingRoomInfo.ratingLimit)}</span>
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
              {sameGenderMemberList.map((info) => (
                <WaitingMemberBox
                  key={info.member.memberId}
                  nickname={info.member.nickname}
                  rating={info.member.rating}
                  matchCnt={info.member.meetingCount}
                  gender={info.member.gender}
                  profileImageSrc={info.member.profileImageSrc}
                />
              ))}
            </div>
            <div className="waiting-room-content">
              {diffGenderMemberList.map((info) => (
                <WaitingMemberBox
                  key={info.member.memberId}
                  nickname={info.member.nickname}
                  rating={info.member.rating}
                  matchCnt={info.member.meetingCount}
                  gender={info.member.gender}
                  profileImageSrc={info.member.profileImageSrc}
                />
              ))}
            </div>
          </div>
          <ChatRoom />
        </div>
        <div id="waiting-room-footer">
          {/* "시작하기" 버튼을 방장인 경우에만 렌더링 */}
          {memberList.some(isRoomLeaderInMemberList) && (
            <FilledButton content="시작하기" classes="btn start-btn" handleClick={handleStartBtn} />
          )}
          <FilledButton content="나가기" classes="btn exit-btn" handleClick={handleExitBtn} />
        </div>
      </main>
    </div>
  );
}

export default WaitingRoomPage;
