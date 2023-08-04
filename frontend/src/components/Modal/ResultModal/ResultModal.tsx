import React, { useRef, useState } from 'react';
import './ResultModal.css';
import RightSignal from '../../../assets/image/right_signal.png';

interface ResultModalProps {
  onClose: () => void;
}

function ResultModal({ onClose }: ResultModalProps) {
  return (
    <div className="result-modal">
      <div className="result-header">
        <img className="signal right" src={RightSignal} alt="시그널 아이콘" />
      </div>
      <div className="result-content">
        <h1 className="matching-result">매칭결과</h1>
        <label id="label-result" htmlFor="label-result">
          매칭에 {} 했습니다.
        </label>
      </div>
      <div className="content">{/* <ProfileImg> */}</div>
      <div className="pointer">{/* {Point} */}</div>
    </div>
  );
}

export default ResultModal;
