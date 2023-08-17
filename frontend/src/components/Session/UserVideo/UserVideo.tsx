import React, { useState, useRef, useEffect } from 'react';
import OvVideo from '../OvVideo/OvVideo';
import { StreamManager } from 'openvidu-browser';
import './UserVideo.css';
import { useStream } from '../../../hooks/useStrem';
import { useAppSelector } from '../../../app/hooks';
import axios from 'axios';
import InfoElement from '../../Element/InfoElement';
import { useDispatch } from 'react-redux';
import { setFinalSignalReceiver } from '../../../app/slices/meetingSlice';

// import * as tf from '@tensorflow/tfjs';
// import * as blazeface from '@tensorflow-models/blazeface';
import * as faceapi from 'face-api.js';
import drawLandmarks from './drawLandmarks'; // 수정된 drawLandmarks 파일 경로

import eyeImageSrc from '../../../assets/image/eye_mask.png';

interface UserVideoProps {
  streamManager: StreamManager;
  nickname: string;
  signalOpen: boolean;
  userInfoOpen: boolean;
  job: string;
  year: string;
}

// 유저의 화상 화면을 보여주는 컴포넌트
function UserVideo({
  streamManager,
  nickname,
  signalOpen,
  userInfoOpen,
  job,
  year,
}: UserVideoProps) {
  const { videoRef, speaking } = useStream(streamManager);
  const { roomId } = useAppSelector((state) => state.waitingRoom);
  const { memberId } = useAppSelector((state) => state.user);
  const { finalSignalReceiver } = useAppSelector((state) => state.meetingRoom);
  const [isSendSignal, setIsSendSignal] = useState(false);

  const dispatch = useDispatch();

  const canvasRef = useRef<HTMLCanvasElement | null>(null); // 이 부분을 상위 scope에서 초기화
  const [videoWidth, setVideoWidth] = useState(0);
  const [videoHeight, setVideoHeight] = useState(0);

  const handleMetadataLoad = () => {
    const videoElement = videoRef.current;
    if (videoElement) {
      setVideoWidth(videoElement.videoWidth); // 비디오 너비를 상태에 설정
      setVideoHeight(videoElement.videoHeight); // 비디오 높이를 상태에 설정
    }
  };
  useEffect(() => {
    // 비디오 엘리먼트 메타데이터 로드 시점에 실행될 함수

    console.log(videoHeight + ' ' + videoWidth + 'hhhhhhhhhhhhhhhhhhhhhhh');
    // 비디오 엘리먼트에 loadedmetadata 이벤트 리스너 추가
    const videoElement = videoRef.current;
    if (videoElement) {
      videoElement.addEventListener('loadedmetadata', handleMetadataLoad);
    }

    async function setupCamera() {
      const video = videoRef.current!;
      const stream = await navigator.mediaDevices.getUserMedia({ video: {} });
      video.srcObject = stream;

      return new Promise<void>((resolve) => {
        video.onloadedmetadata = () => {
          resolve();
        };
      });
    }

    async function loadFaceAPI() {
      await faceapi.nets.tinyFaceDetector.loadFromUri('/models'); // 얼굴 감지 모델 로드
      await faceapi.nets.faceLandmark68Net.loadFromUri('/models'); // 랜드마크 감지 모델 로드
    }

    async function detectFaceAndDrawOverlay() {
      const video = videoRef.current!;
      video.play();

      video.addEventListener('canplay', async () => {
        const canvas = canvasRef.current!;

        if (!canvas) {
          return; // canvas가 유효하지 않으면 중지
        }

        canvas.width = videoWidth;
        canvas.height = videoHeight;

        const displaySize = { width: videoWidth, height: videoHeight };
        console.log('===============' + videoWidth + ' ' + videoHeight);
        const faceCanvas = faceapi.createCanvasFromMedia(video);
        document.body.append(faceCanvas);

        const ctx = canvas.getContext('2d')!;
        const faceCtx = faceCanvas.getContext('2d')!;

        // Overlay image on eyes (example: 'eye_image.png')
        const eyeImage = new Image();
        // import('../../../assets/image/eye_mask.png').then((image) => {
        //   setRankImg(image.default);
        // });
        eyeImage.src = eyeImageSrc;
        console.log('=========================================' + eyeImage.src);
        await eyeImage.onload; // 이미지 로드가 완료될 때까지 대기

        console.log(eyeImage.src);
        async function detectFace() {
          const faceDetections = await faceapi.detectAllFaces(
            video,
            new faceapi.TinyFaceDetectorOptions(),
          );

          faceCtx.clearRect(0, 0, faceCanvas.width, faceCanvas.height);

          if (faceDetections.length > 0) {
            // const face = faceDetections[0];
            const landmarks = await faceapi.detectFaceLandmarks(video);

            // Draw landmarks on the faceCanvas
            if (landmarks instanceof faceapi.FaceLandmarks68) {
              const leftEye = landmarks.getLeftEye();
              const rightEye = landmarks.getRightEye();

              drawLandmarks(faceCtx, leftEye);
              drawLandmarks(faceCtx, rightEye);

              // 이미지 그리기 작업
              const eyeWidth = rightEye[3].x - leftEye[0].x;
              const eyeHeight = (rightEye[4].y + rightEye[5].y) / 2 - rightEye[1].y; // 수정된 부분
              const eyeX = leftEye[0].x;
              const eyeY = leftEye[1].y - eyeHeight / 2;

              // console.log(eyeWidth + ' ' + eyeHeight + 'eyeeeeeeeeeeeeeeeeeeeee');

              ctx.drawImage(eyeImage, eyeX, eyeY, eyeWidth, eyeHeight);
            }
          }
          requestAnimationFrame(detectFace);
        }
        detectFace();
      });
    }

    async function main() {
      await setupCamera();
      await loadFaceAPI();
      detectFaceAndDrawOverlay();
    }

    main();
    // 언마운트 시 이벤트 리스너 제거
    return () => {
      if (videoElement) {
        videoElement.removeEventListener('loadedmetadata', handleMetadataLoad);
      }
    };
  }, [videoRef]);

  const handleSendSignal = async () => {
    console.log(finalSignalReceiver);
    if (
      finalSignalReceiver === 0 &&
      confirm('한 번 선택하면 취소할 수 없습니다.\n이분에게 마음을 전하시겠습니까?')
    ) {
      const streamData = JSON.parse(streamManager.stream.connection.data);
      const signalData = {
        signalSequence: 2,
        senderId: memberId,
        receiverId: streamData.memberId,
      };

      console.log('시그널 전송');
      console.log(signalData);

      // 서버로 시그널 데이터 전송
      try {
        const res = await axios.post(`/api/rooms/${roomId}/signals`, signalData);

        if (res.status === 200) {
          alert('당신의 마음이 성공적으로 전달되었습니다.\n그 마음이 이어지길 응원합니다.');
          setIsSendSignal(true);
          dispatch(setFinalSignalReceiver(streamData.memberId));
        }
      } catch (err) {
        alert('다시 시도해주세요.');
        console.error(err);
      }
    }
  };

  return (
    <OvVideo nickname={nickname} speaking={speaking}>
      <video className="stream-video" ref={videoRef}>
        <track kind="captions"></track>
      </video>
      <canvas className="face-canvas" ref={canvasRef}></canvas>
      {signalOpen && (
        <button className="meeting-signal" onClick={handleSendSignal}>
          <span className={`material-symbols-outlined ${isSendSignal ? 'sended' : ''}`}>
            favorite
          </span>
        </button>
      )}
      {userInfoOpen && <InfoElement job={job} age={year} />}
    </OvVideo>
  );
}

export default UserVideo;
