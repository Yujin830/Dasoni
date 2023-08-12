import React from 'react';
import RankAvartar from '../../Avarta/RankAvartar/RackAvartar';
import './SuccessResult.css';

interface SuccessResultProps {
  profileSrc: string | undefined;
  rating: number | undefined;
  otherProfileSrc: string | undefined;
  otherRating: number | undefined;
}

function SuccessResult({ profileSrc, rating, otherProfileSrc, otherRating }: SuccessResultProps) {
  return (
    <div id="success-result">
      <RankAvartar profileSrc={profileSrc} point={rating} />
      <span className="material-symbols-outlined heart">favorite</span>
      <RankAvartar profileSrc={otherProfileSrc} point={otherRating} />
    </div>
  );
}

export default SuccessResult;
