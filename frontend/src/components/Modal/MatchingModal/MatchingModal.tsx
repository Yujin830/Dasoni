import React, { useState, useEffect } from 'react';
import './MatchingModal.css';
import axios from 'axios';
import { useAppSelector } from '../../../app/hooks';
import { useNavigate } from 'react-router';
import { Hearts } from 'react-loader-spinner';

interface MatchingModalProps {
  onClose: () => void;
}

const MatchingModal: React.FC<MatchingModalProps> = ({ onClose }) => {
  const member = useAppSelector((state) => state.user);
  const navigate = useNavigate();
  const [isMatching, setIsMatching] = useState<boolean>(true);
  const [isMatched, setIsMatched] = useState<boolean>(false);

  const eventSource = new EventSource(`api/alarm/subscribe/${member.memberId}`);

  eventSource.addEventListener('match', (event: MessageEvent) => {
    const parseData = JSON.parse(event.data);

    if (parseData.status === 'OK') {
      setIsMatching(false);
      setIsMatched(true);

      setTimeout(() => {
        navigateToMeetingRoom(parseData.roomId, parseData.megi);
      }, 3000);
    }
  });

  const navigateToMeetingRoom = (roomId: string, isMegi: boolean) => {
    navigate(`/meeting/${roomId}`, {
      replace: true,
      state: { isMegi },
    });
    eventSource.close();
    onClose();
  };

  eventSource.onopen = () => {
    // console.log('onopen', eventSource.readyState);
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
    <div className="matchingmodal-overlay active">
      <div className="matching-modal">
        <h2>{isMatching ? '매칭중...' : isMatched ? '매칭완료!' : ''}</h2>
        <Hearts color="red" />
        {isMatched && <div>3초 후에 방으로 이동합니다.</div>}
        {isMatched ? null : (
          <div className="matching-button">
            <button className="close-button" onClick={handleCancel}>
              매칭 취소
            </button>
          </div>
        )}
      </div>
    </div>
  );
};

export default MatchingModal;
