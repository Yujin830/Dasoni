import React, { useMemo } from 'react';
import { useOpenvidu } from '../../hooks/useOpenvidu';
import { useParams } from 'react-router';
import UserVideo from '../../components/Session/UserVideo/UserVideo';
import './MeetingPage.css';
import { useAppSelector } from '../../app/hooks';
import ToolBar from '../../components/ToolBar/ToolBar';

function MeetingPage() {
  const { roomId } = useParams();
  const { memberId, nickname, gender } = useAppSelector((state) => state.user);
  const { publisher, streamList, onChangeCameraStatus, onChangeMicStatus } = useOpenvidu(
    memberId !== undefined ? memberId : 0,
    nickname !== undefined ? nickname : '',
    roomId !== undefined ? roomId : '1',
    gender !== undefined ? gender : '',
  );
  console.log(publisher);
  console.log(streamList);

  // const memberList = useMemo(() => streamList, [streamList]);

  // 현재 로그인한 유저와 다른 성별의 memberList
  const diffGenderMemberList = useMemo(
    () => streamList.filter((stream) => stream.gender !== gender),
    [streamList],
  );

  //현재 로그인한 유저와 같은 성별의 memberList
  const sameGenderMemberList = useMemo(
    () => streamList.filter((stream) => stream.gender === gender),
    [streamList],
  );
  return (
    <div id="meeting">
      <div id="meeting-video-container">
        <div className="meeting-video-row">
          {publisher &&
            sameGenderMemberList.map((stream, index) => (
              <UserVideo
                streamManager={stream.streamManager}
                nickname={stream.nickname}
                key={index}
              />
            ))}
        </div>
        <div className="meeting-video-row">
          {publisher &&
            diffGenderMemberList.map((stream, index) => (
              <UserVideo
                streamManager={stream.streamManager}
                nickname={stream.nickname}
                key={index}
              />
            ))}
        </div>
        <ToolBar
          onChangeCameraStatus={onChangeCameraStatus}
          onChangeMicStatus={onChangeMicStatus}
        />
      </div>
    </div>
  );
}

export default MeetingPage;
