import { Point } from 'face-api.js';

function drawLandmarks(context: CanvasRenderingContext2D, landmarks: Point[]) {
  context.strokeStyle = '#FF0000'; // 빨간색 선으로 표시
  context.lineWidth = 2;

  landmarks.forEach((point) => {
    context.beginPath();
    context.arc(point.x, point.y, 2, 0, 2 * Math.PI);
    context.stroke();
  });
}

export default drawLandmarks;
