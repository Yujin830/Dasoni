import React from 'react';
import '../input.css';

type BasicInputProps = {
  style: object;
  label: string;
  type: string;
  value: string;
  handleChange: (event: React.ChangeEvent<HTMLInputElement>) => void;
  placeholer?: string;
};

function BasicInput({ style, label, type, value, handleChange, placeholer }: BasicInputProps) {
  return (
    <div className="input-box">
      <label className="label">{label}</label>
      <input
        className="input"
        style={style}
        type={type}
        value={value}
        onChange={handleChange}
        placeholder={placeholer}
      />
    </div>
  );
}

export default BasicInput;
