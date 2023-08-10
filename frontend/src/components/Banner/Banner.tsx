import React from 'react';
import './Banner.css';
import signal from '../../assets/image/white_signal.png';

function Banner() {
  return (
    <div id="banner">
      <div id="text-box">
        <h2>
          <span className="t1">첫</span>
          <span className="t2">인</span>
          <span className="t3">상</span>
          <span className="t4">을 </span>
          <span className="t5">결</span>
          <span className="t6">정</span>
          <span className="t7">하</span>
          <span className="t8">는 </span>
          <br className="mobile" />
          <span className="highlight">3초!</span>
        </h2>
        <h3>
          <span className="t11">나만의 </span>
          <span className="t12">매력을 </span>
          <span className="t13">어필하고 </span>
          <span className="t14">마음에 </span>
          <span className="t15">드는 </span>
          <span className="t16">이성에게 </span>
          {/* 나만의 매력을 어필하고 마음에 드는 이성에게 */}
          <span className="highlight">
            시그널
            <img src={signal} alt="시그널 이미지" />
          </span>
          <span className="t17">을 보내보세요!</span>
          {/* 을 보내보세요! */}
        </h3>
      </div>
    </div>
  );
}

export default Banner;
