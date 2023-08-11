import React from 'react';
import Spinner from '../../assets/image/GIF/Spinner-1.gif';

const Loading01 = () => {
  return (
    <div className="loading01">
      <h3>잠시만 기다려주세요.</h3>
      <img src={Spinner} alt="로딩" />
    </div>
  );
};

export default Loading01;
