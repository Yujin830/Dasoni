import React, { useEffect, useState } from 'react';
import './BasicAvartar.css';

type BasicAvartarProps = {
  src: string;
};

function BasicAvartar({ src }: BasicAvartarProps) {
  const [img, setImg] = useState('');

  const loadImage = (imageName: string) => {
    import(`../../../assets/image/${imageName}`).then((image) => {
      setImg(image.default);
    });
  };

  useEffect(() => {
    loadImage(src);
  }, []);

  return <img className="basic-avartar" src={img} alt="아바타 이미지" />;
}

export default BasicAvartar;
