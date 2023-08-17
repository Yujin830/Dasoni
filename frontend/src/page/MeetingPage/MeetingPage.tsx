import React, { useEffect, useMemo, useState, useRef } from 'react';
import { useOpenvidu } from '../../hooks/useOpenvidu';
import { useNavigate, useParams } from 'react-router';
import UserVideo from '../../components/Session/UserVideo/UserVideo';
import './MeetingPage.css';
import { useAppSelector } from '../../app/hooks';
import ToolBar from '../../components/ToolBar/ToolBar';
import { useWebSocket } from '../../hooks/useWebSocket';
import TimeDisplay from '../../components/Element/TimeDisplay';
import Guide from '../../components/MeetingPage/Guide/Guide';
import Question from '../../components/MeetingPage/Question/Question';
import AudioController from '../../components/AudioController/AudioController';
import WhisperChatRoom from '../../components/ChatRoom/WhisperChatRoom';
import axios from 'axios';
import { setMeetingCount, setRating, setRemainLife } from '../../app/slices/user';
import { setMatchMemberId, setRatingChange, setMeetingRoomId } from '../../app/slices/meetingSlice';
import { useDispatch } from 'react-redux';
import { useLocation } from 'react-router';

function MeetingPage() {
  const location = useLocation();
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
  const [guideMessage, setGuideMessage] = useState(
    '다소니에 오신 여러분 환영합니다. 처음 만난 서로에게 자기소개를 해 주세요.',
  );
  const [currentTime, setCurrentTime] = useState('00:00'); // 타이머 state
  const [startSec, setStartSec] = useState(''); // 서버에서 받아온 시작 시간
  const [question, setQuestion] = useState(''); // 질문 저장
  const [isShow, setIsShow] = useState(true); // 가이드 보이기 / 안 보이기
  const [isQuestionTime, setIsQuestionTime] = useState(false); // 질문 보이기 / 안 보이기
  const [userInfoOpen, setUserInfoOepn] = useState(false); // 유저 정보 보이기 / 안 보이기
  const [firstSignal, setFirstSignal] = useState(false); // 첫인상 투표 보이기 / 안 보이기
  const [signalOpen, setSignalOpen] = useState(false); // 최종 선택 시그널 보이기 / 안 보이기
  const [requestResult, setRequestResult] = useState(false); // 최종 개인 결과 요청 가능 / 요청 불가능
  const [isMegiFlag, setIsMegiFlag] = useState(false);
  const [hasSentMegiMessage, setHasSentMegiMessage] = useState(false);

  const navigate = useNavigate();
  const dispatch = useDispatch();

  const client = useWebSocket({
    subscribe: (client) => {
      //질문 만들어주는 메소드
      client.subscribe(`/topic/room/${roomId}/makeQuestion`, (res: any) => {
        console.log(res.body);
      });
      // 메기 입장 시간 close
      client.subscribe(`/topic/room/${roomId}/endMegi`, (res: any) => {
        console.log('endMegiTime');
      });
      // 가이드 구독

      client.subscribe(`/topic/room/${roomId}/megiEnterMessage`, (res: any) => {
        console.log('enterMegi!!!');
        console.log(res.body);
        setGuideMessage('메기 등장!!! 메기 등장!!! 메기가 등장합니다!!');
        setIsShow(true);
      });

      // 가이드 구독
      client.subscribe(`/topic/room/${roomId}/guide`, (res: any) => {
        setGuideMessage(res.body);
        setIsShow(true);
        setIsQuestionTime(false);
      });

      // 질문 구독
      client.subscribe(`/topic/room/${roomId}/questions`, (res: any) => {
        console.log('질문 ', res.body);
        setQuestion(res.body);
        setIsQuestionTime(true);
      });

      // 첫인상 투표 구독
      client.subscribe(`/topic/room/${roomId}/firstSignal`, (res: any) => {
        setFirstSignal(true);
      });

      // 유저 정보 공개 구독
      client.subscribe(`/topic/room/${roomId}/open`, (res: any) => {
        console.log(res.body);
        setUserInfoOepn(true);
      });

      // 최종 시그널 오픈 구독
      client.subscribe(`/topic/room/${roomId}/signal`, (res: any) => {
        console.log(res.body);
        setSignalOpen(true);
      });

      // 최종 개인 결과 요청 가능 구독
      client.subscribe(`/topic/room/${roomId}/requestResult`, (res: any) => {
        console.log(res.body);
        setRequestResult(true);
      });

      // 메기 입장 가능 결과 구독
      client.subscribe(`/topic/room/${roomId}/megi`, (res: any) => {
        console.log(res.body);
      });

      // 메기 입장 완료 구독
      client.subscribe(`/topic/room/${roomId}/megiEnter`, (res: any) => {
        console.log(res.body);
      });
    },
    onClientReady: (client) => {
      const time: string[] = currentTime.split(':');
      const minutes = time[0];
      const seconds = time[1];
      // console.log(client);
      // console.log(`${minutes} ${seconds}`);

      if (minutes === '05' && seconds === '00') {
        client?.send(`/app/room/${roomId}/guide`, {}, '5');
      }

      // 가이드 - 첫인상 투표
      else if (minutes === '00' && seconds === '30') {
        client?.send(`/app/room/${roomId}/guide`, {}, '5');
      }

      // 첫인상 투표창 공개
      else if (minutes === '00' && seconds === '35') {
        client?.send(`/app/room/${roomId}/firstSignal`);
      }

      // 가이드 - 정보 공개
      else if (minutes === '01' && seconds === '10') {
        client?.send(`/app/room/${roomId}/guide`, {}, '20');
      }

      // 메기 입장
      else if (minutes === '01' && seconds === '15') {
        client?.send(`/app/room/${roomId}/megi`, {}, 'megigo');
      }

      // 유저 정보 공개
      else if (minutes === '01' && seconds === '35') {
        client?.send(`/app/room/${roomId}/open`);
      }
      //질문 생성. 반드시!!! 랜덤주제보다 먼저 실행할것.
      else if (minutes === '01' && seconds === '20') {
        client?.send(`/app/room/${roomId}/makeQuestion`);
      }

      // 랜덤 주제 1번
      else if (minutes === '01' && seconds === '40') {
        client?.send(`/app/room/${roomId}/questions`, {}, '0');
      }
      // 랜덤 주제 2번
      else if (minutes === '01' && seconds === '50') {
        client?.send(`/app/room/${roomId}/questions`, {}, '1');
      }

      // 랜덤 주제 3번
      else if (minutes === '02' && seconds === '15') {
        client?.send(`/app/room/${roomId}/questions`, {}, '2');
      } else if (minutes === '02' && seconds === '20') {
        client?.send(`/app/room/${roomId}/endMegi`);
      }

      // 가이드 - 최종 투표
      else if (minutes === '02' && seconds === '25') {
        client?.send(`/app/room/${roomId}/guide`, {}, '50');
      }

      // 최종 시그널 메세지 open send
      else if (minutes === '02' && seconds === '30') {
        client?.send(`/app/room/${roomId}/signal`);
      } else if (isMegiFlag && !hasSentMegiMessage) {
        setTimeout(() => {
          client?.send(`/app/room/${roomId}/megiEnterMessage`);
          setHasSentMegiMessage(true);
        }, 5000); // 5초 후 실행
      }
    },
  });

  const audioRef = useRef<HTMLAudioElement | null>(null);
  // Volume and Mute Controls
  const [volume, setVolume] = useState(1);
  const [muted, setMuted] = useState(false);
  useEffect(() => {
    dispatch(setMeetingRoomId(roomId));
  }, [roomId, dispatch]);

  useEffect(() => {
    if (location.state?.isMegi) {
      console.log('isMegi?', location.state.isMegi);
      console.log('isMegi is true', roomId);
      setIsMegiFlag(true);

      // 예) 특정 알림 표시, 데이터 요청 등의 로직
    } else {
      console.log('ismegi?', false);
    }
  }, [location.state]);

  // 서버 시간으로 타이머 설정
  useEffect(() => {
    fetchElapsedTime();
  }, []);

  const fetchElapsedTime = async () => {
    try {
      const response = await axios.get(`/api/rooms/${roomId}/elapsedTime`);
      console.log('시간', response.data);
      setStartSec(response.data);
    } catch (error) {
      console.error('Failed to fetch elapsed time:', error);
    }
  };

  useEffect(() => {
    if (audioRef.current) {
      audioRef.current.volume = volume;
      audioRef.current.muted = muted;
    }
  }, [volume, muted]);

  // 가이드 닫기
  useEffect(() => {
    if (isShow) {
      setTimeout(() => {
        setIsShow(false);
      }, 5000);
    }
  }, [isShow]);

  // 채팅 투표 닫기
  useEffect(() => {
    if (firstSignal) {
      setTimeout(() => {
        setFirstSignal(false);
      }, 20000);
    }
  }, [firstSignal]);

  // 최종 투표 닫기 & 전체 미팅 결과 계산 요청
  useEffect(() => {
    if (signalOpen) {
      setTimeout(async () => {
        setSignalOpen(false);

        // 전체 결과 계산 요청
        const res = await axios.delete(`/api/rooms/${roomId}`);
        console.log(res.data);
        if (res.data.status.code === 7000) {
          client?.send(`/app/room/${roomId}/requestResult`);
        }
      }, 30000);
    }
  }, [signalOpen]);

  // 개인 투표 결과 요청 실행
  useEffect(() => {
    if (requestResult) {
      requestPersonalResult();
    }
  }, [requestResult]);

  // 개인 투표 결과 요청
  const requestPersonalResult = async () => {
    const res = await axios.get(`/api/rooms/${roomId}/members/${memberId}`);
    console.log(res.data);

    const data = res.data;
    dispatch(setRating(data.content.roomMemberInfo.member.rating)); // 변경 후 레이팅 저장
    dispatch(setRatingChange(data.content.ratingChange)); // 레이팅 변화값 저장
    dispatch(setMeetingCount(data.content.roomMemberInfo.member.meetingCount)); // 미팅 카운트 증가
    dispatch(setRemainLife(data.content.remainLife)); // 라이프 감소
    if (data.content.matchMemberId !== 0) {
      // 미팅 결과 저장
      dispatch(setMatchMemberId(data.content.matchMemberId)); // 매칭된 상대방 저장
      navigate(`/sub-meeting/${roomId}`, { replace: true });
    } else {
      setGuideMessage(
        '안타깝지만 마음이 이어지지 않았습니다.\n다음은 마음이 이어지기를 응원하겠습니다.\n3초후 세션이 종료 됩니다.',
      );
      setIsShow(true);
      setTimeout(() => {
        navigate('/result', { replace: true });
      }, 5000);
    }
  };

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
      <TimeDisplay currentTime={currentTime} startSec={startSec} setCurrentTime={setCurrentTime} />
      <Guide isShow={isShow} guideMessage={guideMessage} />

      <div id="meeting-video-container">
        {isQuestionTime && <Question content={question} />}
        <div className="meeting-video-row">
          {publisher &&
            sameGenderMemberList.map((stream, index) => (
              <UserVideo
                streamManager={stream.streamManager}
                nickname={stream.nickname}
                job={stream.job}
                year={stream.year}
                signalOpen={false}
                userInfoOpen={userInfoOpen}
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
                job={stream.job}
                year={stream.year}
                signalOpen={signalOpen}
                userInfoOpen={userInfoOpen}
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
        {firstSignal && <WhisperChatRoom diffGenderMemberList={diffGenderMemberList} />}
      </div>
    </div>
  );
}

export default MeetingPage;
