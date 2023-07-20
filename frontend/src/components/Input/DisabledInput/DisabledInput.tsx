import React from 'react';
import '../input.css';

type DisabledInputProps = {
  style: object;
  label: string;
};

function DisabledInput({ style, label }: DisabledInputProps) {
  return (
    <div className="input-box">
      <label className="label">{label}</label>
      <input className="input disabled" style={style} disabled />
    </div>
  );
}

export default DisabledInput;
