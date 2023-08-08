import React, { useEffect, useMemo, useState, useRef } from 'react';
import { useOpenvidu } from '../../hooks/useOpenvidu';
import { useParams } from 'react-router';
import UserVideo from '../../components/Session/UserVideo/UserVideo';
import './MeetingPage.css';
import { useAppSelector } from '../../app/hooks';
import ToolBar from '../../components/ToolBar/ToolBar';
import { useWebSocket } from '../../hooks/useWebSocket';
import TimeDisplay from '../../components/Element/TimeDisplay';
import Guide from '../../components/MeetingPage/Guide/Guide';
import Question from '../../components/MeetingPage/Question/Question';
import song from '../../assets/music/meeting.mp3';
import ChatRoom from '../../components/ChatRoom/ChatRoom';

function MeetingPage() {
  const audioRef = useRef<HTMLAudioElement | null>(null);
  const { roomId } = useParams();
  const { memberId, nickname, gender } = useAppSelector((state) => state.user);
  const { publisher, streamList, onChangeCameraStatus, onChangeMicStatus } = useOpenvidu(
    memberId !== undefined ? memberId : 0,
    nickname !== undefined ? nickname : '',
    roomId !== undefined ? roomId : '1',
    gender !== undefined ? gender : '',
  );

  const [guideMessage, setGuideMessage] = useState(
    '다소니에 오신 여러분 환영합니다. 처음 만난 서로에게 자기소개를 해 주세요.',
  );
  const [question, setQuestion] = useState('');
  const [isShow, setIsShow] = useState(true);
  const [isQuestionTime, setIsQuestionTime] = useState(false);

  // Volume and Mute Controls
  const [volume, setVolume] = useState(1);
  const [muted, setMuted] = useState(false);

  const client = useWebSocket({
    subscribe: (client) => {
      // 가이드 구독
      client.subscribe(`/topic/room/${roomId}/guide`, (res: any) => {
        setGuideMessage(res.body);
        setIsShow(true);
        setIsQuestionTime(false);
      });

      // 질문 구독
      client.subscribe(`/topic/room/${roomId}/questions`, (res: any) => {
        setQuestion(res.body);
        setIsQuestionTime(true);
      });

      // TODO : 첫인상 투표 구독

      // TODO : 최종 투표 구독
    },
  });

  useEffect(() => {
    if (audioRef.current) {
      audioRef.current.volume = volume;
      audioRef.current.muted = muted;
    }
  }, [volume, muted]);

  useEffect(() => {
    if (isShow) {
      setTimeout(() => {
        setIsShow(false);
      }, 5000);
    }
  }, [isShow]);

  const handleVolumeChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setVolume(Number(e.target.value));
  };

  const handleMuteToggle = () => {
    setMuted((prevMuted) => !prevMuted);
  };

  const diffGenderMemberList = useMemo(
    () => streamList.filter((stream) => stream.gender !== gender),
    [streamList],
  );

  const sameGenderMemberList = useMemo(
    () => streamList.filter((stream) => stream.gender === gender),
    [streamList],
  );

  return (
    <div id="meeting">
      <TimeDisplay client={client} roomId={roomId} />
      <Guide isShow={isShow} guideMessage={guideMessage} />

      <div id="meeting-video-container">
        {isQuestionTime && <Question content={question} />}
        <div className="meeting-video-row">
          {publisher &&
            sameGenderMemberList.map((stream, index) => (
              <UserVideo
                streamManager={stream.streamManager}
                nickname={stream.nickname}
                key={index}
              />
            ))}
        </div>
        <div className="meeting-video-row">
          {publisher &&
            diffGenderMemberList.map((stream, index) => (
              <UserVideo
                streamManager={stream.streamManager}
                nickname={stream.nickname}
                key={index}
              />
            ))}
        </div>
        <ToolBar
          onChangeCameraStatus={onChangeCameraStatus}
          onChangeMicStatus={onChangeMicStatus}
        />
      </div>
      <div id="audio-controls">
        <input
          type="range"
          min="0"
          max="1"
          step="0.01"
          value={volume}
          onChange={handleVolumeChange}
        />
        <button onClick={handleMuteToggle}>{muted ? 'Unmute' : 'Mute'}</button>
        <ChatRoom />
      </div>

      {/* eslint-disable-next-line jsx-a11y/media-has-caption */}
      <audio ref={audioRef} src={song} loop autoPlay={true} />
    </div>
  );
}

export default MeetingPage;
