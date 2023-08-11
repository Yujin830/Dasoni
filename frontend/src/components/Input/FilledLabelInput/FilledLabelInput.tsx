import React from 'react';
import '../input.css';
import './FilledLabelInput.css';

type FiledLabelProps = {
  classes?: string;
  style?: object;
  label: string;
  type: string;
  value: string;
  handleChange: (event: React.ChangeEvent<HTMLInputElement>) => void;
  placeholer: string;
};

function FiledLabelInput({
  style,
  label,
  type,
  value,
  handleChange,
  placeholer,
  classes,
}: FiledLabelProps) {
  return (
    <div className="input-box">
      <label className="filled-bg">{label}</label>
      <input
        className={`input ${classes}`}
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
