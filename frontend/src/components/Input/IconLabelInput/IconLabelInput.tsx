import React from 'react';
import '../input.css';
import './IconLabelInput.css';

type IconLabelInputProps = {
  style: object;
  label: string;
  icon: string;
  type: string;
  value: string;
  handleChange: (event: React.ChangeEvent<HTMLInputElement>) => void;
  placeholer: string;
};

function IconLabelInput({
  style,
  label,
  icon,
  type,
  value,
  handleChange,
  placeholer,
}: IconLabelInputProps) {
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
      <span className="material-symbols-outlined icon">{icon}</span>
    </div>
  );
}

export default IconLabelInput;
