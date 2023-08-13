import React, { useEffect } from 'react';
import './MatchingModal.css';
import axios from 'axios';
import { useAppSelector } from '../../../app/hooks';
import { useNavigate } from 'react-router';

interface MatchingModalProps {
  onClose: () => void;
}

const MatchingModal: React.FC<MatchingModalProps> = ({ onClose }) => {
  const member = useAppSelector((state) => state.user);
  const navigate = useNavigate();
  const eventSource = new EventSource(`api/alarm/subscribe/${member.memberId}`);
  console.log('멤버아이디 ', member.memberId);

  eventSource.addEventListener('match', (event) => {
    const { data: receivedConnectData } = event;
    console.log('connect!!', receivedConnectData);

    const res = event.data;
    const parseData = JSON.parse(res);
    console.log(parseData.roomId);
    console.log(parseData.status);
    console.log(parseData.readyState);

    if (parseData.status === 'OK') {
      console.log('Matched Room ID:', parseData.roomId);
      alert(`매칭 완료! 방 ID: ${parseData.roomId}. 3초 후에 방으로 이동합니다.`);

      setTimeout(() => {
        navigate(`/meeting/${parseData.roomId}`, { replace: true });
        eventSource.close();
        onClose();
      }, 3000);
    } else {
      console.log(res.message);
    }
    console.log(eventSource.readyState);
  });

  eventSource.onopen = () => {
    console.log('onopen', eventSource.readyState);
  };

  eventSource.onerror = (event: any) => {
    console.error('SSE error:', event);
  };

  const handleCancel = async () => {
    try {
      const res = await axios.delete(`/api/match/members/${member.memberId}`);
      console.log('status', res.data);
      onClose();
    } catch (error) {
      console.error('Error canceling the matching:', error);
    }
  };

  useEffect(() => {
    return () => {
      eventSource.close(); // 컴포넌트 언마운트 시 SSE 연결 종료
    };
  }, []);

  return (
    <div className="openroommodal-overlay active">
      <div className="openroom-modal">
        <div className="header">매칭중</div>
        <div className="box">
          <div className="box-content">
            <div className="box-title-content">현재 매칭중입니다.</div>
            <div className="openroom-button">
              <button className="close-button" onClick={handleCancel}>
                매칭 취소
              </button>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default MatchingModal;
