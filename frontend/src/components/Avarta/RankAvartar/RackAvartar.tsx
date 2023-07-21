import React, { useEffect, useState } from 'react';
import './RankAvartar.css';

type RankAvartarProps = {
  src: string;
  point: number;
};

function RankAvartar({ src, point }: RankAvartarProps) {
  const [profileImg, setProfileImg] = useState('');
  const [rankImg, setRankImg] = useState('');

  const parsePointToRankImage = (point: number) => {
    let rank = '';
    if (point < 100) rank = 'white';
    else if (point < 300) rank = 'yellow';
    else if (point < 700) rank = 'green';
    else if (point < 1500) rank = 'purple';
    else if (point < 3000) rank = 'blue';
    else if (point < 4000) rank = 'red';
    else rank = 'rainbow';
    return rank + '.png';
  };

  const loadImage = (imageName: string, type: string) => {
    if (type === 'profile') {
      import(`../../../assets/image/${imageName}`).then((image) => {
        setProfileImg(image.default);
      });
    } else {
      import(`../../../assets/image/heart/${imageName}`).then((image) => {
        setRankImg(image.default);
      });
    }
  };

  useEffect(() => {
    loadImage(src, 'profile');
    loadImage(parsePointToRankImage(point), 'rank');
  }, []);

  return (
    <span className="rank-avartar">
      <img className="profile" src={profileImg} alt="아바타 이미지" />
      <img className="rank" src={rankImg} alt="랭크 하트 이미지" />
    </span>
  );
}

export default RankAvartar;
