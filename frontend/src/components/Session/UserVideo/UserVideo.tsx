import React from 'react';
import OvVideo from '../OvVideo/OvVideo';
import { StreamManager } from 'openvidu-browser';

type UserVideoProps = {
  streamManager: StreamManager;
};

function UserVideo({ streamManager }: UserVideoProps) {
  const getNicknameTag = () => {
    return JSON.parse(streamManager.stream.connection.data).clientData;
  };

  return (
    <div>
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
