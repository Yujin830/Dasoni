import React from 'react';
import './ExpPointBar.css';

type ExpPointBarProps = {
  percent: number; // 경험치 바의 퍼센트 값 (0~100 사이의 숫자)
  points: number | undefined; // 현재까지 쌓은 포인트
  style?: React.CSSProperties; // 옵셔널한 스타일 객체
};

function ExpPointBar({ percent, points, style }: ExpPointBarProps) {
  const barWidth = `${percent}%`;

  return (
    <div className="exp-point-box">
      <ExpPointLabel points={points} />
      <div className="exp-point-bar" style={{ ...style }}>
        <div className="exp-point-fill" style={{ width: barWidth }} />
      </div>
    </div>
  );
}

type ExpPointLabelProps = {
  points: number | undefined; // 현재까지 쌓은 포인트
};

function ExpPointLabel({ points }: ExpPointLabelProps) {
  return <div className="exp-point-label">{points}</div>;
}

export default ExpPointBar;
