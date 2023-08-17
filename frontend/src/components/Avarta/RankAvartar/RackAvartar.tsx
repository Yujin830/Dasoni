import React, { useEffect, useState } from 'react';
import './RankAvartar.css';
import { useAppSelector } from '../../../app/hooks';

type RankAvartarProps = {
  profileSrc: string | undefined;
  point: number | undefined;
};

function RankAvartar({ profileSrc, point }: RankAvartarProps) {
  // const { profileImageSrc } = useAppSelector((state) => state.user);
  // const [profileImg, setProfileImg] = useState('');
  const [rankImg, setRankImg] = useState('');

  const parsePointToRankImage = (point: number) => {
    let rank = '';
    if (point < 0) rank = 'black';
    else if (point < 100) rank = 'white';
    else if (point < 300) rank = 'yellow';
    else if (point < 700) rank = 'green';
    else if (point < 1500) rank = 'purple';
    else if (point < 3000) rank = 'blue';
    else if (point < 4000) rank = 'red';
    else rank = 'rainbow';
    return rank + '.png';
  };

  const loadImage = (imageName: string) => {
    import(`../../../assets/image/heart/${imageName}`).then((image) => {
      setRankImg(image.default);
    });
  };

  useEffect(() => {
    if (point !== undefined) loadImage(parsePointToRankImage(point));
  }, []);

  return (
    <span className="rank-avartar">
      <img className="profile" src={profileSrc} alt="아바타 이미지" />
      <img className="rank" src={rankImg} alt="랭크 하트 이미지" />
    </span>
  );
}

export default RankAvartar;
