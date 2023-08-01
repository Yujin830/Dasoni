import axios from 'axios';
import { OpenVidu } from 'openvidu-browser';
import { useCallback, useEffect, useMemo, useState } from 'react';

// 서버 주소를 환경에 따라 설정
const APPLICATION_SERVER_URL = process.env.NODE_ENV === 'production' ? '' : 'http://localhost:4443';
const OPENVIDU_SERVER_SECRET = 'MY_SECRET';

export const useOpenvidu = (memberId: number, meetingRoomId: string) => {
  const [subscribers, setSubscribers] = useState<any[]>([]);
  const [publisher, setPublisher] = useState<any>();
  const [session, setSession] = useState<any>();

  const leaveSession = useCallback(() => {
    if (session) session.disconnect();

    setSession(null);
    setPublisher(null);
    setSubscribers([]);
  }, [session]);

  // 세션에 연결 후 비디오 생성하고 발행하는 처리
  useEffect(() => {
    const openVidu = new OpenVidu();
    const session = openVidu.initSession();
    console.log('session 초기화');

    // 스트림 생성 이벤트 구독
    // 스트림 생성 시 subscriber 세팅
    session.on('streamCreated', (event) => {
      console.log(event);
      const subscriber = session.subscribe(event.stream, undefined);
      setSubscribers((subscribers) => [...subscribers, subscriber]);
    });

    // 스트림 삭제 이벤트 구독
    // 스트림 삭제시 subscriber 삭제
    session.on('streamDestroyed', (event) => {
      deleteSubscriber(event.stream.streamManager);
    });

    session.on('exception', (exception) => {
      console.warn(exception);
    });

    // OpenVidu 배포에서 토큰 얻기
    if (session) {
      getToken(meetingRoomId).then(async (token) => {
        try {
          console.log(token);
          // 획득한 토큰으로 세션에 연결
          await session.connect(token, JSON.stringify({ memberId }));
          await navigator.mediaDevices.getUserMedia({ audio: true, video: true });
          const devices = await openVidu.getDevices();
          const videoDevices = devices.filter((device) => device.kind === 'videoinput');
          const publisher = openVidu.initPublisher('', {
            audioSource: undefined,
            videoSource: videoDevices[0].deviceId,
            publishAudio: true,
            publishVideo: true,
            resolution: '640x480',
            frameRate: 30,
            insertMode: 'APPEND',
            mirror: true,
          });

          setPublisher(publisher);
          session.publish(publisher);
        } catch (err: any) {
          console.log('There was an error connecting to the session:', err.code, err.message);
        }
      });
    }

    setSession(session);
    return () => {
      if (session) session.disconnect();
      setSession(null);
      setPublisher(null);
      setSubscribers([]);
    };
  }, []);

  // subscriber 삭제
  const deleteSubscriber = useCallback((streamManager: any) => {
    setSubscribers((prevSubscribers) => {
      const index = prevSubscribers.indexOf(streamManager);
      if (index > -1) {
        const newSubscribers = [...prevSubscribers];
        newSubscribers.splice(index, 1);
        return newSubscribers;
      } else {
        return prevSubscribers;
      }
    });
  }, []);

  useEffect(() => {
    window.addEventListener('beforeunload', () => leaveSession());

    return () => {
      window.removeEventListener('beforeunload', () => leaveSession());
    };
  }, [leaveSession]);

  const onChangeCameraStatus = useCallback(
    (status: boolean) => {
      publisher?.publishVideo(status);
    },
    [publisher],
  );

  const onChangeMicStatus = useCallback(
    (status: boolean) => {
      publisher?.publishAudio(status);
    },
    [publisher],
  );

  const streamList = useMemo(
    () => [{ streamManager: publisher, memberId }, ...subscribers],
    [publisher, subscribers, memberId],
  );

  return {
    publisher,
    streamList,
    onChangeCameraStatus,
    onChangeMicStatus,
  };
};

// 세션 연결을 위해 토큰 가져오는 함수
const getToken = async (roomId: string) => {
  return createSession(roomId).then((roomId) => createToken(roomId));
};

// 세션 생성
const createSession = async (roomId: string) => {
  try {
    const res = await axios.post(
      `${APPLICATION_SERVER_URL}/openvidu/api/sessions`,
      { customSessionId: roomId },
      {
        headers: {
          Authorization: 'Basic ' + btoa('OPENVIDUAPP:' + OPENVIDU_SERVER_SECRET),
          'Content-Type': 'application/json',
        },
      },
    );

    if (res.status == 200) {
      console.log('세션 생성', res);
      console.log(res.data.sessionId);
      return res.data.sessionId; // sessionId 반환
    }
  } catch (err) {
    console.error(err);
  }
};

// 토큰 생성
const createToken = async (sessionId: string) => {
  try {
    console.log('sessionId ' + sessionId);
    const res = await axios.post(
      `${APPLICATION_SERVER_URL}/openvidu/api/sessions/${sessionId}/connection`,
      {},
      {
        headers: {
          Authorization: 'Basic ' + btoa('OPENVIDUAPP:' + OPENVIDU_SERVER_SECRET),
          'Content-Type': 'application/json',
        },
      },
    );

    if (res.status == 200) {
      console.log(res);
      return res.data.token; // 토큰 반환
    }
  } catch (err) {
    console.error(err);
  }
};
