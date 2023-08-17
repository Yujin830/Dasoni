import React, { useEffect, useState } from 'react';
import './ToolBar.css';
import { useNavigate, useParams } from 'react-router';
import axios from 'axios';
import { useAppSelector } from '../../app/hooks';
import { useDispatch } from 'react-redux';
import { setMeetingCount, setRemainLife } from '../../app/slices/user';

interface ToolBarProps {
  onChangeCameraStatus: (status: boolean) => void;
  onChangeMicStatus: (status: boolean) => void;
}

function ToolBar({ onChangeCameraStatus, onChangeMicStatus }: ToolBarProps) {
  const [micStatus, setMicStatus] = useState(true);
  const [cameraStatus, setCameraStatus] = useState(true);
  const navigate = useNavigate();
  const { roomId } = useAppSelector((state) => state.meetingRoom);
  const { memberId, rating } = useAppSelector((state) => state.user);
  const pathName = location.pathname.split('/')[1];

  const dispatch = useDispatch();

  const handleMicStatus = () => {
    setMicStatus((prev) => !prev);
  };

  const handleCameraStatus = () => {
    setCameraStatus((prev) => !prev);
  };

  const handleExitBtn = async () => {
    if (confirm('미팅 중 퇴장 시 패널티를 받습니다.\n정말 나가시겠습니까?')) {
      // 라이프 감소, 미팅 수 증가, 레이팅 마이너스
      const res = await axios.get(`/api/members/${memberId}`);
      console.log(res.data);
      dispatch(setRemainLife(res.data.content.remainLife));
      dispatch(setMeetingCount(res.data.content.meetingCount));
      // 메인으로 이동
      navigate('/result', { replace: true });
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
      {pathName !== 'meeting' && (
        <button onClick={handleExitBtn}>
          <span className="material-symbols-outlined filled">logout</span>
        </button>
      )}
    </div>
  );
}

export default ToolBar;
