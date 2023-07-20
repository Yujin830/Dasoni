import React from 'react';
import './InfoElement.css';

type InfoElementProps = {
  job: string;
  age: number;
};

function InfoElement({ job, age }: InfoElementProps) {
  return (
    <div className="info-container">
      <span className="info-job">{job}</span>
      <span className="info-age">{age}</span>
    </div>
  );
}

export default InfoElement;
