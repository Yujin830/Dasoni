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
import ChatRoom from '../../components/ChatRoom/ChatRoom';
import AudioController from '../../components/AudioController/AudioController';
import WhisperChatRoom from '../../components/ChatRoom/WhisperChatRoom';

function MeetingPage() {
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
  const [question, setQuestion] = useState(''); // 질문 저장
  const [isShow, setIsShow] = useState(true); // 가이드 보이기 / 안 보이기
  const [isQuestionTime, setIsQuestionTime] = useState(false); // 질문 보이기 / 안 보이기
  const [signalOpen, setSignalOpen] = useState(false); // 최종 선택 시그널 보이기 / 안 보이기

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
      client.subscribe(`/topic/room/${roomId}/signal`, (res: any) => {
        console.log(res.body);
        setSignalOpen(true);
      });
    },
  });

  const audioRef = useRef<HTMLAudioElement | null>(null);
  // Volume and Mute Controls
  const [volume, setVolume] = useState(1);
  const [muted, setMuted] = useState(false);
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
                signalOpen={false}
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
                signalOpen={signalOpen}
                key={index}
              />
            ))}
        </div>
        <ToolBar
          onChangeCameraStatus={onChangeCameraStatus}
          onChangeMicStatus={onChangeMicStatus}
        />
      </div>
      <div>
        <AudioController
          volume={volume}
          muted={muted}
          songName="meeting"
          handleMuteToggle={handleMuteToggle}
          handleVolumeChange={handleVolumeChange}
        />
        <WhisperChatRoom diffGenderMemberList={diffGenderMemberList} />
      </div>
    </div>
  );
}

export default MeetingPage;
