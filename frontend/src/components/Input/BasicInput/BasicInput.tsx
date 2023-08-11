import React from 'react';
import '../input.css';
import './BasicInput.css';

type BasicInputProps = {
  classes?: string;
  labelClass?: string;
  style?: object;
  label?: string;
  type: string;
  value?: string;
  handleChange?: (event: React.ChangeEvent<HTMLInputElement>) => void;
  placeholer?: string;
};

function BasicInput({
  style,
  label,
  type,
  value,
  handleChange,
  placeholer,
  classes,
  labelClass,
}: BasicInputProps) {
  return (
    <div className="input-box">
      <label className={`${labelClass !== undefined ? labelClass : 'label'}`}>{label}</label>
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

export default BasicInput;
