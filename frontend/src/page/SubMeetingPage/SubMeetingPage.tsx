import React, { useMemo } from 'react';
import TimeDisplay from '../../components/Element/TimeDisplay';
import { useAppSelector } from '../../app/hooks';
import { useOpenvidu } from '../../hooks/useOpenvidu';
import UserVideo from '../../components/Session/UserVideo/UserVideo';
import ChatRoom from '../../components/ChatRoom/ChatRoom';
import ToolBar from '../../components/ToolBar/ToolBar';
import './SubMeetingPage.css';

function SubMeetingPage() {
  //   const { roomId } = useAppSelector((state) => state.waitingRoom);
  const { memberId, nickname, gender } = useAppSelector((state) => state.user);
  const { publisher, streamList, onChangeCameraStatus, onChangeMicStatus } = useOpenvidu(
    memberId !== undefined ? memberId : 0,
    nickname !== undefined ? nickname : '',
    '1',
    gender !== undefined ? gender : '',
  );

  const me = useMemo(
    () => streamList.filter((stream: any) => stream.gender === gender),
    [streamList],
  );

  const other = useMemo(
    () => streamList.filter((stream: any) => stream.gender !== gender),
    [streamList],
  );

  return (
    <div id="sub-meeting">
      <div className="container">
        <div className="video-container">
          <div className="video-row me">
            {publisher &&
              me.map((stream, index) => (
                <UserVideo
                  key={index}
                  nickname={stream.nickname}
                  streamManager={stream.streamManager}
                />
              ))}
          </div>
          <div className="video-row other">
            {publisher &&
              other.map((stream, index) => (
                <UserVideo
                  key={index}
                  nickname={stream.nickname}
                  streamManager={stream.streamManager}
                />
              ))}
          </div>
          <ToolBar
            onChangeCameraStatus={onChangeCameraStatus}
            onChangeMicStatus={onChangeMicStatus}
          />
        </div>
        <ChatRoom />
      </div>
    </div>
  );
}

export default SubMeetingPage;
