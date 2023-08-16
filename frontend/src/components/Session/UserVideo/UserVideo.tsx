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

  const handleSendSignal = async () => {
    const canvasRef = useRef<HTMLCanvasElement | null>(null);

    useEffect(() => {
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
        await faceapi.nets.tinyFaceDetector.loadFromUri('/models'); // tinyFaceDetector 모델 로드
      }

      async function detectFaceAndDrawOverlay() {
        const video = videoRef.current!;
        const canvas = canvasRef.current!;
        canvas.width = video.width;
        canvas.height = video.height;

        const displaySize = { width: video.width, height: video.height };
        const faceCanvas = faceapi.createCanvasFromMedia(video);
        document.body.append(faceCanvas);

        const ctx = canvas.getContext('2d')!;
        const faceCtx = faceCanvas.getContext('2d')!;

        video.play();

        async function detectFace() {
          const faceDetections = await faceapi.detectAllFaces(
            video,
            new faceapi.TinyFaceDetectorOptions(),
          );

          faceCtx.clearRect(0, 0, faceCanvas.width, faceCanvas.height);

          if (faceDetections.length > 0) {
            const face = faceDetections[0];
            const landmarks = await faceapi.detectFaceLandmarks(video);

            // Draw landmarks on the faceCanvas
            if (landmarks instanceof faceapi.FaceLandmarks68) {
              const leftEye = landmarks.getLeftEye();
              const rightEye = landmarks.getRightEye();

              drawLandmarks(faceCtx, leftEye);
              drawLandmarks(faceCtx, rightEye);

              // Overlay image on eyes (example: 'eye_image.png')
              const eyeImage = new Image();
              eyeImage.src = '/path/to/eye_image.png';

              eyeImage.onload = () => {
                const eyeWidth = rightEye[3].x - leftEye[0].x;
                const eyeHeight = (leftEye[4].y + leftEye[5].y) / 2 - leftEye[1].y;
                const eyeX = leftEye[0].x;
                const eyeY = leftEye[1].y - eyeHeight / 2;

                ctx.drawImage(eyeImage, eyeX, eyeY, eyeWidth, eyeHeight);
              };
            }
          }

          requestAnimationFrame(detectFace);
        }

        detectFace();
      }

      async function main() {
        await setupCamera();
        await loadFaceAPI();
        detectFaceAndDrawOverlay();
      }

      main();
    }, []);

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
