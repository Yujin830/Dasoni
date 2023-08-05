import React, { useMemo } from 'react';
import { useOpenvidu } from '../../hooks/useOpenvidu';
import { useParams } from 'react-router';
import UserVideo from '../../components/Session/UserVideo/UserVideo';
import './MeetingPage.css';
import { useAppSelector } from '../../app/hooks';

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

  const memberList = useMemo(() => streamList, [streamList]);
  return (
    <div id="meeting">
      <div id="meeting-video-container">
        {publisher &&
          memberList.map((stream, index) => (
            // TODO : 로그인한 멤버의 nickname store에서 가져와서 보여주기
            <UserVideo
              streamManager={stream.streamManager}
              nickname={stream.nickname}
              key={index}
            />
          ))}
      </div>
    </div>
  );
}

export default MeetingPage;
