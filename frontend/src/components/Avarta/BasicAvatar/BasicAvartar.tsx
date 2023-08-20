import React from 'react';
import './BasicAvartar.css';

type BasicAvartarProps = {
  src: string | undefined;
};

function BasicAvartar({ src }: BasicAvartarProps) {
  return <img className="basic-avartar" src={src} alt="아바타 이미지" />;
}

export default BasicAvartar;
