import React from 'react';
import RankAvartar from '../../Avarta/RankAvartar/RackAvartar';

interface FailResult {
  profileSrc: string | undefined;
}

function FailResult({ profileSrc }: FailResult) {
  return (
    <div id="faile-result">
      <RankAvartar profileSrc={profileSrc} point={-1} />
    </div>
  );
}

export default FailResult;
