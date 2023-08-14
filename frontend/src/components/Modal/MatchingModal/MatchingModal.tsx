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

  eventSource.addEventListener('match', (event: MessageEvent) => {
    const parseData = JSON.parse(event.data);

    if (parseData.status === 'OK') {
      const confirmMsg = `매칭 완료! 3초 후에 방으로 이동합니다.`;
      setTimeout(() => {
        window.confirm(confirmMsg);
        navigateToMeetingRoom(parseData.roomId);
      }, 3000);
    }
  });

  const navigateToMeetingRoom = (roomId: string) => {
    navigate(`/meeting/${roomId}`, { replace: true });
    eventSource.close();
    onClose();
  };

  eventSource.onopen = () => {
    console.log('onopen', eventSource.readyState);
  };

  eventSource.onerror = (event: Event) => {
    console.error('SSE error:', event);
  };

  const handleCancel = async () => {
    try {
      await axios.delete(`/api/match/members/${member.memberId}`);
      onClose();
    } catch (error) {
      console.error('Error canceling the matching:', error);
    }
  };

  useEffect(() => {
    return () => {
      eventSource.close();
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
