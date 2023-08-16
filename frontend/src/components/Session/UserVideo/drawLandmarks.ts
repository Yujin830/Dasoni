// drawLandmarks.ts
import { Point } from './Point'; // 현재 파일과 동일한 경로에 있는 Point.ts 파일

function drawLandmarks(ctx: CanvasRenderingContext2D, landmarks: Point[]) {
  // draw landmarks using ctx
  landmarks.forEach((landmark) => {
    ctx.beginPath();
    ctx.arc(landmark.x, landmark.y, 2, 0, 2 * Math.PI);
    ctx.fillStyle = 'red';
    ctx.fill();
  });
}

export default drawLandmarks;
