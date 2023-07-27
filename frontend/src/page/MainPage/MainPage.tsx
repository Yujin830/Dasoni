import React, { useCallback, useEffect, useRef, useState } from 'react';
import Header from '../../components/Header/Header';
import { OpenVidu } from 'openvidu-browser';
import axios from 'axios';
import UserVideo from '../../components/Session/UserVideo/UserVideo';
import './MainPage.css';

// 서버 주소를 환경에 따라 설정
const APPLICATION_SERVER_URL =
  process.env.NODE_ENV === 'production' ? '' : 'https://demos.openvidu.io/';

function MainPage() {
  // 세션과 비디오 엘리먼트 관리하는 state
  const [mySessionId, setMySessionId] = useState('SessionA');
  const [myUserName, setMyUserName] = useState(`Participant${Math.floor(Math.random() * 100)}`);
  const [session, setSession] = useState<any>(undefined);
  const [mainStreamManager, setMainStreamManager] = useState<any>(undefined);
  const [publisher, setPublisher] = useState<any>(undefined);
  const [subscribers, setSubscribers] = useState<any[]>([]);
  const [currentVideoDevice, setCurrentVideoDevice] = useState<any>(null);

  // OpenVidu 객체의 레퍼런스 생성
  const OV = useRef<OpenVidu>(new OpenVidu());

  // sessionId 설정하는 핸들러
  const handleChangeSessionId = useCallback((e: any) => {
    setMySessionId(e.target.value);
  }, []);

  // 사용자 이름 변경하는 핸들러
  const handleChangeUserName = useCallback((e: any) => {
    setMyUserName(e.target.value);
  }, []);

  // 메인 비디오 스트림 설정 처리하는 핸들러
  const handleMainVideoStream = useCallback(
    (stream: any, event: React.MouseEvent<HTMLAnchorElement>) => {
      event.preventDefault();
      if (mainStreamManager !== stream) {
        setMainStreamManager(stream);
      }
    },
    [mainStreamManager],
  );

  // 세션 참여하는 함수
  const joinSession = useCallback(() => {
    // OpenVidu 세션 생성
    const mySession = OV.current.initSession();

    // 스트림 생성 이벤트 구독
    // 스트림 생성 시 subscriber 세팅
    mySession.on('streamCreated', (event) => {
      console.log(event);
      const subscriber = mySession.subscribe(event.stream, undefined);
      setSubscribers((subscribers) => [...subscribers, subscriber]);
    });

    // 스트림 삭제 이벤트 구독
    // 스트림 삭제시 subscriber 삭제
    mySession.on('streamDestroyed', (event) => {
      deleteSubscriber(event.stream.streamManager);
    });

    mySession.on('exception', (exception) => {
      console.warn(exception);
    });

    setSession(mySession);
  }, []);

  // 세션에 연결 후 비디오 생성하고 발행하는 처리
  useEffect(() => {
    if (session) {
      // OpenVidu 배포에서 토큰 얻기
      getToken().then(async (token: any) => {
        try {
          // 획득한 토큰으로 세션에 연결
          await session.connect(token, { clientData: myUserName });

          // 발행할 비디오 생성
          const publisher = await OV.current.initPublisherAsync(undefined, {
            audioSource: undefined,
            videoSource: undefined,
            publishAudio: true,
            publishVideo: true,
            resolution: '320x240',
            frameRate: 30,
            insertMode: 'APPEND',
            mirror: false,
          });

          session.publish(publisher);

          // 사용 가능한 비디오 디바이스 얻고 현재 비디오 디바이스 설정
          const devices = await OV.current.getDevices();
          const videoDevices = devices.filter((device) => device.kind === 'videoinput');
          const currentVideoDeviceId = publisher.stream
            .getMediaStream()
            .getVideoTracks()[0]
            .getSettings().deviceId;
          const currentVideoDevice = videoDevices.find(
            (device) => device.deviceId === currentVideoDeviceId,
          );

          setMainStreamManager(publisher);
          setPublisher(publisher);
          setCurrentVideoDevice(currentVideoDevice);
        } catch (error: any) {
          console.log('There was an error connecting to the session:', error.code, error.message);
        }
      });
    }
  }, [session, myUserName]);

  // 세션 종료하기
  const leaveSession = useCallback(() => {
    // 세션이 있으면 연결 끊기
    if (session) {
      session.disconnect();
    }

    // 모든 state와 OpenVidu 객체 초기화
    OV.current = new OpenVidu();
    setSession(undefined);
    setSubscribers([]);
    setMySessionId('SessionA');
    setMyUserName('Participant' + Math.floor(Math.random() * 100));
    setMainStreamManager(undefined);
    setPublisher(undefined);
  }, [session]);

  // 카메라 전환 처리
  const switchCamera = useCallback(async () => {
    try {
      const devices = await OV.current.getDevices();
      const videoDevices = devices.filter((device) => device.kind === 'videoinput');

      if (videoDevices && videoDevices.length > 1) {
        const newVideoDevice = videoDevices.filter(
          (device) => device.deviceId !== currentVideoDevice.deviceId,
        );

        if (newVideoDevice.length > 0) {
          const newPublisher = OV.current.initPublisher(undefined, {
            videoSource: newVideoDevice[0].deviceId,
            publishAudio: true,
            publishVideo: true,
            mirror: true,
          });

          if (session) {
            await session.unpublish(mainStreamManager);
            await session.publish(newPublisher);
            setCurrentVideoDevice(newVideoDevice[0]);
            setMainStreamManager(newPublisher);
            setPublisher(newPublisher);
          }
        }
      }
    } catch (e) {
      console.error(e);
    }
  }, [currentVideoDevice, session, mainStreamManager]);

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
    const handleBeforeUnload = (event: any) => {
      leaveSession();
    };
    window.addEventListener('beforeunload', handleBeforeUnload);

    return () => {
      window.removeEventListener('beforeunload', handleBeforeUnload);
    };
  }, [leaveSession]);

  // 세션 연결을 위해 토큰 가져오는 함수
  const getToken = useCallback(async () => {
    return createSession(mySessionId).then((sessionId) => createToken(sessionId));
  }, [mySessionId]);

  // 세션 생성
  const createSession = async (sessionId: any) => {
    const response = await axios.post(
      APPLICATION_SERVER_URL + 'api/sessions',
      { customSessionId: sessionId },
      {
        headers: { 'Content-Type': 'application/json' },
      },
    );
    return response.data; // sessionId 반환
  };

  // 토큰 생성
  const createToken = async (sessionId: any) => {
    const response = await axios.post(
      APPLICATION_SERVER_URL + 'api/sessions/' + sessionId + '/connections',
      {},
      {
        headers: { 'Content-Type': 'application/json' },
      },
    );
    return response.data; // 토큰 반환
  };

  return (
    <div id="main">
      <Header />
      <main>
        <div className="container">
          {session === undefined ? (
            <div id="join">
              <div id="img-div">
                <img
                  src="resources/images/openvidu_grey_bg_transp_cropped.png"
                  alt="OpenVidu logo"
                />
              </div>
              <div id="join-dialog" className="jumbotron vertical-center">
                <h1> Join a video session </h1>
                <form className="form-group" onSubmit={joinSession}>
                  <p>
                    <label htmlFor="userName">Participant: </label>
                    <input
                      className="form-control"
                      type="text"
                      id="userName"
                      value={myUserName}
                      onChange={handleChangeUserName}
                      required
                    />
                  </p>
                  <p>
                    <label htmlFor="sessionId"> Session: </label>
                    <input
                      className="form-control"
                      type="text"
                      id="sessionId"
                      value={mySessionId}
                      onChange={handleChangeSessionId}
                      required
                    />
                  </p>
                  <p className="text-center">
                    <input
                      className="btn btn-lg btn-success"
                      name="commit"
                      type="submit"
                      value="JOIN"
                    />
                  </p>
                </form>
              </div>
            </div>
          ) : null}

          {session !== undefined ? (
            <div id="session">
              <div id="session-header">
                <h1 id="session-title">{mySessionId}</h1>
                <input
                  className="btn btn-large btn-danger"
                  type="button"
                  id="buttonLeaveSession"
                  onClick={leaveSession}
                  value="Leave session"
                />
                <input
                  className="btn btn-large btn-success"
                  type="button"
                  id="buttonSwitchCamera"
                  onClick={switchCamera}
                  value="Switch Camera"
                />
              </div>

              {/* {mainStreamManager !== undefined ? (
                <div id="main-video" className="col-md-6">
                  <UserVideo streamManager={mainStreamManager} />
                </div>
              ) : null} */}
              <div id="video-container" className="col-md-6">
                {publisher !== undefined ? (
                  <a
                    href="/"
                    className="stream-container col-md-6 col-xs-6"
                    onClick={(e) => handleMainVideoStream(publisher, e)}
                  >
                    <UserVideo streamManager={publisher} />
                  </a>
                ) : null}
                {subscribers.map((sub, i) => (
                  <a
                    href="/"
                    key={sub.id}
                    className="stream-container col-md-6 col-xs-6"
                    onClick={(e) => handleMainVideoStream(sub, e)}
                  >
                    <span>{sub.id}</span>
                    <UserVideo streamManager={sub} />
                  </a>
                ))}
              </div>
            </div>
          ) : null}
        </div>
      </main>
    </div>
  );
}

export default MainPage;
