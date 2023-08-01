import React from 'react';
import { useOpenvidu } from '../../hooks/useOpenvidu';
import { useParams } from 'react-router';
import Header from '../../components/Header/Header';

function MeetingPage() {
  const { roomId } = useParams();
  const { publisher, streamList, onChangeCameraStatus, onChangeMicStatus } = useOpenvidu(
    1,
    roomId !== undefined ? roomId : '1',
  );
  console.log(publisher);
  console.log(streamList);
  return (
    <div id="meeting">
      <Header />
      <p>미팅 페이지</p>
    </div>
  );
}

export default MeetingPage;
