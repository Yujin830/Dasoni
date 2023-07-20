import React from 'react';
import './ControlBox.css';

type ControlBoxProps = {
  style: object;
  type: string;
  value: string;
};

function ControlBox({ style, type, value }: ControlBoxProps) {
  return <input className="control-box" style={style} type={type} value={value} />;
}

export default ControlBox;
