import React from 'react';
import './Banner.css';
import signal from '../../assets/image/white_signal.png';

function Banner() {
  return (
    <div id="banner">
      <div id="text-box">
        <h2>
          첫인상을 결정하는 <span className="highlight">3초!</span>
        </h2>
        <h3>
          나만의 매력을 어필하고 마음에 드는 이성에게
          <span className="highlight">
            시그널
            <img src={signal} alt="시그널 이미지" />
          </span>
          을 보내보세요!
        </h3>
      </div>
    </div>
  );
}

export default Banner;
