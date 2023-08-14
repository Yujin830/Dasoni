import React from 'react';
import RankAvartar from '../../Avarta/RankAvartar/RackAvartar';
import './FailResult.css';

interface FailResult {
  profileSrc: string | undefined;
}

function FailResult({ profileSrc }: FailResult) {
  return (
    <div id="fail-result">
      <RankAvartar profileSrc={profileSrc} point={-1} />
    </div>
  );
}

export default FailResult;
