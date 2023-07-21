import React from 'react';
import './ControlBox.css';

type ControlBoxProps = {
  style: object;
};

function ControlBox({ style }: ControlBoxProps) {
  return <input className="control-box" />;
}

export default ControlBox;
