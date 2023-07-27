import React from 'react';
import OvVideo from '../OvVideo/OvVideo';
import { StreamManager } from 'openvidu-browser';
import './UserVideo.css';

type UserVideoProps = {
  streamManager: StreamManager;
};

// 유저의 화상 화면을 보여주는 컴포넌트
function UserVideo({ streamManager }: UserVideoProps) {
  const getNicknameTag = () => {
    return JSON.parse(streamManager.stream.connection.data).clientData;
  };

  return (
    <div className="user-video">
      {streamManager !== undefined ? (
        <div>
          <OvVideo streamManager={streamManager} />
          <div>
            <p>{getNicknameTag()}</p>
          </div>
        </div>
      ) : null}
    </div>
  );
}

export default UserVideo;
