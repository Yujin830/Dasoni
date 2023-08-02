import React, { useRef, useState } from 'react';
import '../Modal.css';
import './HelpModal.css';

interface HelpModalProps {
  onClose: () => void;
}

function HelpModal({ onClose }: HelpModalProps) {
  return (
    <div className="modal">
      <div className="header">
        도움말
        <div className="close-button">
          <button onClick={onClose}>Close</button>
        </div>
      </div>
      <div className="box">
        <h1>1. 메기 입장하기</h1>
        <div className="explain">
          메기는 느슨한 미팅 씬에 긴장을 주는 플레이어입니다. 메기는 미팅 시작 20분 후부터 입장이
          가능합니다. 늦게 참여한만큼 불리하기도 하겠지만 모두의 관심과 이목을 한 번에 받을 수
          있으며 메기로 참여해 시그널을 받을 경우 더 높은 가중치의 레이팅 점수를 받게 됩니다! 이
          점을 잘 이용하여 메기로 이성들의 시그널을 받으세요!
        </div>
        <div className="content">메기 입장 보여주는 GIF</div>
        <svg
          className="pagenation"
          xmlns="http://www.w3.org/2000/svg"
          width="147"
          height="15"
          viewBox="0 0 147 15"
          fill="none"
        >
          <circle cx="7.5" cy="7.5" r="7.5" fill="#EC5E98" />
          <circle cx="40.5" cy="7.5" r="7.5" fill="#FFE8EF" />
          <circle cx="73.5" cy="7.5" r="7.5" fill="#FFE8EF" />
          <circle cx="106.5" cy="7.5" r="7.5" fill="#FFE8EF" />
          <circle cx="139.5" cy="7.5" r="7.5" fill="#FFE8EF" />
        </svg>
      </div>
      <div className="modal-background" />
    </div>
  );
}

export default HelpModal;
