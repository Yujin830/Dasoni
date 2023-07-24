import React, { useEffect, useState } from 'react';
import './RecentMatchAvartar.css';

type recentMatchAvartar = {
  src: string;
};

function RecentMatchAvartar({ src }: recentMatchAvartar) {
  const [profileImg, setProfileImg] = useState('');

  const loadImage = (imageName: string) => {
    import(`../../../assets/image/${imageName}`).then((image) => {
      setProfileImg(image.default);
    });
  };

  useEffect(() => {
    loadImage(src);
  }, []);

  return (
    <div className="recent-match-avartar">
      <img className="profile" src={profileImg} alt="아바타 이미지" />
      <span className="material-symbols-outlined chat">send</span>
    </div>
  );
}

export default RecentMatchAvartar;
