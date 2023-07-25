import React from 'react';
import '../input.css';
import './FilledLabelInput.css';

type FiledLabelProps = {
  style: object;
  label: string;
  type: string;
  value: string;
  handleChange: (event: React.ChangeEvent<HTMLInputElement>) => void;
  placeholer: string;
};

function FiledLabelInput({ style, label, type, value, handleChange, placeholer }: FiledLabelProps) {
  return (
    <div className="input-box">
      <label className="label filled-bg">{label}</label>
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

export default FiledLabelInput;
