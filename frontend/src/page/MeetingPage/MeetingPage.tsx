import React, { useMemo } from 'react';
import { useOpenvidu } from '../../hooks/useOpenvidu';
import { useParams } from 'react-router';
import UserVideo from '../../components/Session/UserVideo/UserVideo';
import './MeetingPage.css';

function MeetingPage() {
  const { roomId } = useParams();
  const { publisher, streamList, onChangeCameraStatus, onChangeMicStatus } = useOpenvidu(
    1, // TODO : 로그인한 멤버의 memberId로 store에서 가져오기
    roomId !== undefined ? roomId : '1',
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
            <UserVideo streamManager={stream.streamManager} nickname="sunsunny" key={index} />
          ))}
      </div>
    </div>
  );
}

export default MeetingPage;
