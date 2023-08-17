import React, { useEffect, useMemo, useState, useRef } from 'react';
import TimeDisplay from '../../components/Element/TimeDisplay';
import { useAppSelector } from '../../app/hooks';
import { useOpenvidu } from '../../hooks/useOpenvidu';
import UserVideo from '../../components/Session/UserVideo/UserVideo';
import ChatRoom from '../../components/ChatRoom/ChatRoom';
import ToolBar from '../../components/ToolBar/ToolBar';
import './SubMeetingPage.css';
import { useNavigate, useParams } from 'react-router';
import { useWebSocket } from '../../hooks/useWebSocket';
import Guide from '../../components/MeetingPage/Guide/Guide';
import AudioController from '../../components/AudioController/AudioController';
import axios from 'axios';

function SubMeetingPage() {
  const { roomId } = useParams();
  const { memberId, nickname, gender, job, birth } = useAppSelector((state) => state.user);
  const { publisher, streamList, onChangeCameraStatus, onChangeMicStatus } = useOpenvidu(
    memberId !== undefined ? memberId : 0,
    nickname !== undefined ? nickname : '',
    roomId !== undefined ? roomId : '1',
    gender !== undefined ? gender : '',
    job !== undefined ? job : '',
    birth !== undefined ? birth.split('-')[0] : '',
  );

  const [currentTime, setCurrentTime] = useState('00:00'); // 타이머 state
  const [startSec, setStartSec] = useState(''); // 서버에서 받아온 시작 시간
  const [guideMessage, setGuideMessage] = useState(
    '서로의 마음이 닿은 여러분, 이제 마음을 확인하며 둘만의 즐거운 시간 보내기 바랍니다',
  );
  const [isClose, setIsClose] = useState(false);
  const [isShow, setIsShow] = useState(true);
  const navigate = useNavigate();

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
    fetchElapsedTime();
  }, []);

  const fetchElapsedTime = async () => {
    try {
      const response = await axios.get(`/api/rooms/${roomId}/elapsedTime`);
      // const elapsedSeconds = parseInt(response.data, 10);
      console.log('시간', response.data);
      console.log('시간', Number(response.data));
      setStartSec(response.data);
      // console.log('시간', elapsedSeconds);
      // calculateElapsedTime(elapsedSeconds);
    } catch (error) {
      console.error('Failed to fetch elapsed time:', error);
    }
  };

  const handleVolumeChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setVolume(Number(e.target.value));
  };
  const handleMuteToggle = () => {
    setMuted((prevMuted) => !prevMuted);
  };

  useWebSocket({
    subscribe: (client) => {
      // 서브 세션 방 종료
      client.subscribe(`/topic/room/${roomId}/subClose`, (res: any) => {
        console.log(res.body);
        setGuideMessage('3초 후 세션이 종료됩니다.');
        setIsClose(true);
        setIsShow(true);
      });
    },

    onClientReady: (client) => {
      const time: string[] = currentTime.split(':');
      const minutes = time[0];
      const seconds = time[1];

      if (minutes === '00' && seconds === '40') {
        client?.send(`/app/room/${roomId}/subClose`);
      }
    },
  });

  // 가이드 닫기
  useEffect(() => {
    if (isShow) {
      setTimeout(() => {
        setIsShow(false);
      }, 3000);
    }
  }, [isShow]);

  // 세션 종료하기
  useEffect(() => {
    if (isClose) {
      setTimeout(() => {
        navigate('/result', { replace: true });
      }, 3000);
    }
  }, [isClose]);

  const me = useMemo(
    () => streamList.filter((stream: any) => stream.gender === gender),
    [streamList],
  );

  const other = useMemo(
    () => streamList.filter((stream: any) => stream.gender !== gender),
    [streamList],
  );

  return (
    <div id="sub-meeting">
      <TimeDisplay currentTime={currentTime} startSec={startSec} setCurrentTime={setCurrentTime} />
      <Guide isShow={isShow} guideMessage={guideMessage} />
      <div className="container">
        <div className="video-container">
          <div className="video-row me">
            {publisher &&
              me.map((stream, index) => (
                <UserVideo
                  signalOpen={false}
                  userInfoOpen={false}
                  key={index}
                  nickname={stream.nickname}
                  job={''}
                  year={''}
                  streamManager={stream.streamManager}
                />
              ))}
          </div>
          <div className="video-row other">
            {publisher &&
              other.map((stream, index) => (
                <UserVideo
                  signalOpen={false}
                  userInfoOpen={false}
                  key={index}
                  nickname={stream.nickname}
                  job={''}
                  year={''}
                  streamManager={stream.streamManager}
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
            songName="submeeting"
            handleMuteToggle={handleMuteToggle}
            handleVolumeChange={handleVolumeChange}
          />
        </div>
        <ChatRoom />
      </div>
    </div>
  );
}

export default SubMeetingPage;
