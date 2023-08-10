import React from 'react';
import guide_bg from '../../../assets/image/guide_bg.png';
import './Guide.css';

interface GuideProps {
  guideMessage: string;
  isShow: boolean;
}

function Guide({ guideMessage, isShow }: GuideProps) {
  return (
    <div className={`guide ${isShow ? 'show' : ''}`}>
      <img src={guide_bg} alt="구름 이미지" />
      <p>{guideMessage}</p>
    </div>
  );
}

export default Guide;
