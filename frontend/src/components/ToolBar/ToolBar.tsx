import React, { useEffect, useState } from 'react';

interface ToolBarProps {
  onChangeCameraStatus: (status: boolean) => void;
  onChangeMicStatus: (status: boolean) => void;
}

function ToolBar({ onChangeCameraStatus, onChangeMicStatus }: ToolBarProps) {
  const [micStatus, setMicStatus] = useState(true);
  const [cameraStatus, setCameraStatus] = useState(true);

  const handleMicStatus = () => {
    setMicStatus((prev) => !prev);
  };

  const handleCameraStatus = () => {
    setCameraStatus((prev) => !prev);
  };

  useEffect(() => {
    onChangeMicStatus(micStatus);
  }, [micStatus, onChangeMicStatus]);

  useEffect(() => {
    onChangeCameraStatus(cameraStatus);
  }, [cameraStatus, onChangeCameraStatus]);

  return (
    <div id="tool-bar">
      {micStatus ? (
        <button onClick={handleMicStatus}>
          <span className="material-symbols-outlined filled">mic</span>
        </button>
      ) : (
        <button onClick={handleMicStatus}>
          <span className="material-symbols-outlined filled">mic_off</span>
        </button>
      )}
      {cameraStatus ? (
        <button onClick={handleCameraStatus}>
          <span className="material-symbols-outlined filled">videocam</span>
        </button>
      ) : (
        <button onClick={handleCameraStatus}>
          <span className="material-symbols-outlined filled">videocam_off</span>
        </button>
      )}
    </div>
  );
}

export default ToolBar;
