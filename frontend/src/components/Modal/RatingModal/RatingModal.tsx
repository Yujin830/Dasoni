import React, { useRef, useState } from 'react';
import '../Modal.css';
import './RatingModal.css';
import heartRating from '../../../assets/image/heart/heart_rating.png';

interface RatingModalProps {
  onClose: () => void;
}

function RatingModal({ onClose }: RatingModalProps) {
  return (
    <div className="modal">
      <div className="header">
        도움말
        <div className="close-button">
          <button onClick={onClose}>Close</button>
        </div>
      </div>
      <div className="box">
        <div className="explain">
          <br />
          <br />
          <h1>하트 등급</h1>
          <br />
          <br />
          다소니는 하트의 색으로 등급을 나타냅니다. 시그널 점수를 올려 마음에 드는 이성을
          찾아보세요!{' '}
        </div>
        <div className="rating-content">
          <img className="heart-rating" src={heartRating} alt="하트 등급" />
        </div>
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

export default RatingModal;
