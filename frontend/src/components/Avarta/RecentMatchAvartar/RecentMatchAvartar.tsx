import React, { useEffect, useState } from 'react';
import './RecentMatchAvartar.css';

type recentMatchAvartar = {
  src: string;
  gender: string;
};

function RecentMatchAvartar({ src, gender }: recentMatchAvartar) {
  const [profileImg, setProfileImg] = useState('');

  useEffect(() => {
    // 상대가 기본 이미지일 경우 default 이미지 적용
    if (src === 'null') {
      if (gender === 'female')
        setProfileImg('https://signiel-bucket.s3.ap-northeast-2.amazonaws.com/default_woman.jpg');
      else setProfileImg('https://signiel-bucket.s3.ap-northeast-2.amazonaws.com/default_man.jpg');
    }
  }, []);

  return (
    <div className="recent-match-avartar">
      <img className="profile" src={profileImg} alt="아바타 이미지" />
      <span className="material-symbols-outlined chat">send</span>
    </div>
  );
}

export default RecentMatchAvartar;
