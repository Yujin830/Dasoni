import React, { useEffect, useMemo, useState } from 'react';
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

function MeetingPage() {
  const { roomId } = useParams();
  const { memberId, nickname, gender } = useAppSelector((state) => state.user);
  const { publisher, streamList, onChangeCameraStatus, onChangeMicStatus } = useOpenvidu(
    memberId !== undefined ? memberId : 0,
    nickname !== undefined ? nickname : '',
    roomId !== undefined ? roomId : '1',
    gender !== undefined ? gender : '',
  );
  console.log(publisher);
  console.log(streamList);

  const [guideMessage, setGuideMessage] = useState(
    '다소니에 오신 여러분 환영합니다. 처음 만난 서로에게 자기소개를 해 주세요.',
  );
  const [question, setQuestion] = useState('');
  const [isShow, setIsShow] = useState(true);
  const [isQuestionTime, setIsQuestionTime] = useState(false);
  const client = useWebSocket({
    subscribe: (client) => {
      // 가이드 구독
      client.subscribe(`/topic/room/${roomId}/guide`, (res: any) => {
        console.log(res.body);
        setGuideMessage(res.body);
        setIsShow(true);
        setIsQuestionTime(false);
      });

      // 질문 구독
      client.subscribe(`/topic/room/${roomId}/questions`, (res: any) => {
        console.log(res.body);
        setQuestion(res.body);
        setIsQuestionTime(true);
      });

      // TODO : 첫인상 투표 구독

      // TODO : 최종 투표 구독
    },
  });

  // guide 사라지게 하기
  useEffect(() => {
    if (isShow) {
      setTimeout(() => {
        setIsShow(false);
      }, 5000);
    }
  }, [isShow]);

  // 현재 로그인한 유저와 다른 성별의 memberList
  const diffGenderMemberList = useMemo(
    () => streamList.filter((stream) => stream.gender !== gender),
    [streamList],
  );

  //현재 로그인한 유저와 같은 성별의 memberList
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
    </div>
  );
}

export default MeetingPage;
