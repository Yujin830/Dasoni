import React from 'react';
import './InfoElement.css';

interface InfoElementProps {
  job: string;
  age: string;
}

function InfoElement({ job, age }: InfoElementProps) {
  return (
    <div className="info-container">
      <span className="info-job">{job}</span>
      <span className="info-age">{age}</span>
    </div>
  );
}

export default InfoElement;
