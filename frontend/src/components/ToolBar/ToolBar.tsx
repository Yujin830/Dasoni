import React, { useEffect, useState } from 'react';
import './ToolBar.css';
import { useNavigate } from 'react-router';
import axios from 'axios';
import { useAppSelector } from '../../app/hooks';
import { setMeetingCount, setRemainLife } from '../../app/slices/user';
import { useDispatch } from 'react-redux';

interface ToolBarProps {
  onChangeCameraStatus: (status: boolean) => void;
  onChangeMicStatus: (status: boolean) => void;
}

function ToolBar({ onChangeCameraStatus, onChangeMicStatus }: ToolBarProps) {
  const [micStatus, setMicStatus] = useState(true);
  const [cameraStatus, setCameraStatus] = useState(true);
  const navigate = useNavigate();
  const { roomId } = useAppSelector((state) => state.waitingRoom);
  const { memberId, remainLife } = useAppSelector((state) => state.user);

  const dispatch = useDispatch();

  const handleMicStatus = () => {
    setMicStatus((prev) => !prev);
  };

  const handleCameraStatus = () => {
    setCameraStatus((prev) => !prev);
  };

  const handleExitBtn = async () => {
    if (confirm('미팅 중 퇴장 시 패널티를 받습니다.\n정말 나가시겠습니까?')) {
      // 라이프 감소, 미팅 수 증가
      const res = await axios.get(`/api/members/${memberId}`);
      console.log(res.data);
      dispatch(setRemainLife(res.data.content.remainLife));
      dispatch(setMeetingCount(res.data.content.meetingCount));

      // 대기방 나가기 처리
      await axios.delete(`/api/rooms/${roomId}/members/${memberId}`);

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
      <button onClick={handleExitBtn}>
        <span className="material-symbols-outlined filled">logout</span>
      </button>
    </div>
  );
}

export default ToolBar;
