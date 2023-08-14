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
import { setMatchMemberId, setRatingChange } from '../../app/slices/meetingSlice';
import { useDispatch } from 'react-redux';
function MeetingPage() {
  const { roomId } = useParams();
  const { memberId, nickname, gender, job, birth, remainLife } = useAppSelector(
    (state) => state.user,
  );
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
  const [question, setQuestion] = useState(''); // 질문 저장
  const [isShow, setIsShow] = useState(true); // 가이드 보이기 / 안 보이기
  const [isQuestionTime, setIsQuestionTime] = useState(false); // 질문 보이기 / 안 보이기
  const [userInfoOpen, setUserInfoOepn] = useState(false); // 유저 정보 보이기 / 안 보이기
  const [firstSignal, setFirstSignal] = useState(false); // 첫인상 투표 보이기 / 안 보이기
  const [signalOpen, setSignalOpen] = useState(false); // 최종 선택 시그널 보이기 / 안 보이기
  const [requestResult, setRequestResult] = useState(false); // 최종 개인 결과 요청 가능 / 요청 불가능

  const navigate = useNavigate();
  const dispatch = useDispatch();

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

      client.subscribe(`/topic/room/${roomId}/megi`, (res: any) => {
        console.log(res.body);
        setRequestResult(true);
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
      else if (minutes === '01' && seconds === '11') {
        client?.send(`/app/room/${roomId}/megi`, {}, 'megigo');
      }

      // 유저 정보 공개
      else if (minutes === '01' && seconds === '35') {
        client?.send(`/app/room/${roomId}/open`);
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
      }

      // 가이드 - 최종 투표
      else if (minutes === '02' && seconds === '25') {
        client?.send(`/app/room/${roomId}/guide`, {}, '50');
      }

      // 최종 시그널 메세지 open send
      else if (minutes === '02' && seconds === '30') client?.send(`/app/room/${roomId}/signal`);
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
  const { waitingRoomMemberList } = useAppSelector((state) => state.waitingRoom);
  useEffect(() => {
    if (signalOpen) {
      setTimeout(async () => {
        setSignalOpen(false);

        // 방장이면 전체 결과 서버로 조회
        if (waitingRoomMemberList[0].roomLeader) {
          const res = await axios.delete(`/api/rooms/${roomId}`);
          console.log(res.data);
          if (res.data.status.code === 7000) {
            client?.send(`/app/room/${roomId}/requestResult`);
          }
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
    if (data.content.matchMemberId !== 0) {
      // 미팅 결과 저장
      dispatch(setRating(data.content.roomMemberInfo.member.rating)); // 변경 후 레이팅 저장
      dispatch(setRatingChange(data.content.ratingChange)); // 레이팅 변화값 저장
      dispatch(setMatchMemberId(data.content.matchMemberId)); // 매칭된 상대방 저장
      dispatch(setMeetingCount(data.content.roomMemberInfo.member.meetingCount)); // 미팅 카운트 증가
      dispatch(setRemainLife(data.content.remainLife)); // 라이프 감소
      navigate(`/sub-meeting/${roomId}`, { replace: true });
    } else {
      setGuideMessage(
        '안타깝지만 마음이 이어지지 않았습니다.\n다음은 마음이 이어지기를 응원하겠습니다.\n이제 자유롭게 방을 나가셔도 됩니다',
      );
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
      <TimeDisplay currentTime={currentTime} setCurrentTime={setCurrentTime} />
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
