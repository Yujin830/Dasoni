import React, { useEffect, useState } from 'react';
import './ToolBar.css';
import { useNavigate } from 'react-router';
import axios from 'axios';
import { useAppSelector } from '../../app/hooks';

interface ToolBarProps {
  onChangeCameraStatus: (status: boolean) => void;
  onChangeMicStatus: (status: boolean) => void;
}

function ToolBar({ onChangeCameraStatus, onChangeMicStatus }: ToolBarProps) {
  const [micStatus, setMicStatus] = useState(true);
  const [cameraStatus, setCameraStatus] = useState(true);
  const navigate = useNavigate();
  const { roomId } = useAppSelector((state) => state.waitingRoom);
  const { memberId } = useAppSelector((state) => state.user);

  const handleMicStatus = () => {
    setMicStatus((prev) => !prev);
  };

  const handleCameraStatus = () => {
    setCameraStatus((prev) => !prev);
  };

  const handleExitBtn = async () => {
    if (confirm('미팅 중 퇴장 시 패널티를 받습니다.\n정말 나가시겠습니까?')) {
      await axios.delete(`/api/rooms/${roomId}/members/${memberId}`);
      navigate('/main', { replace: true });
    }
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
      <button onClick={handleExitBtn}>
        <span className="material-symbols-outlined filled">logout</span>
      </button>
    </div>
  );
}

export default ToolBar;
