import React, { useState } from 'react';
import '../input.css';

type NoLableInputProps = {
  classes?: string;
  style?: object;
  type: string;
  value: string;
  handleChange: (event: React.ChangeEvent<HTMLInputElement>) => void;
  placeholer: string;
};

function NoLableInput({
  style,
  type,
  value,
  handleChange,
  placeholer,
  classes,
}: NoLableInputProps) {
  return (
    <input
      className={`input ${classes}`}
      style={style}
      type={type}
      value={value}
      onChange={handleChange}
      placeholder={placeholer}
    />
  );
}

export default NoLableInput;
