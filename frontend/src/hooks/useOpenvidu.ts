import axios from 'axios';
import { OpenVidu } from 'openvidu-browser';
import { useCallback, useEffect, useState } from 'react';

// 서버 주소를 환경에 따라 설정
const APPLICATION_SERVER_URL = process.env.NODE_ENV === 'production' ? '' : 'https://localhost';
const OPENVIDU_SERVER_SECRET = 'MY_SECRET';

export const useOpenvidu = (memberId: number, meetingRoomId: number) => {
  const [subscribers, setSubscribers] = useState<any[]>([]);
  const [publisher, setPublisher] = useState<any>();
  const [session, setSession] = useState<any>();

  // 세션에 연결 후 비디오 생성하고 발행하는 처리
  useEffect(() => {
    const openVidu = new OpenVidu();
    const session = openVidu.initSession();

    // OpenVidu 배포에서 토큰 얻기
    if (session) {
      getToken(meetingRoomId).then(async (token) => {
        try {
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
        } catch (err) {
          console.error(err);
        }
      });
    }
  });
};

// 세션 연결을 위해 토큰 가져오는 함수
const getToken = async (roomId: number) => {
  return createSession(roomId).then((roomId) => createToken(roomId));
};

// 세션 생성
const createSession = async (roomId: number) => {
  const res = await axios.post(
    `${APPLICATION_SERVER_URL}/openvidu/api/sessions`,
    { customSessionId: roomId },
    {
      headers: {
        Authorization: 'Basic' + btoa('OPENVIDUAPP:' + OPENVIDU_SERVER_SECRET),
        'Content-Type': 'application/json',
      },
    },
  );
  console.log('세션 생성', res);
  return res.data.id; // sessionId 반환
};

// 토큰 생성
const createToken = async (sessionId: number) => {
  const res = await axios.post(
    `${APPLICATION_SERVER_URL}/openvidu/api/sessions/${sessionId}/connection`,
    {},
    {
      headers: {
        Authorization: 'Basic' + btoa('OPENVIDUAPP:' + OPENVIDU_SERVER_SECRET),
        'Content-Type': 'application/json',
      },
    },
  );

  console.log(res);
  return res.data.token; // 토큰 반환
};
