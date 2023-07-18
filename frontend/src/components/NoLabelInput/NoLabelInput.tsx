import React, { useState } from 'react';
import './NoLabelInput.css';

type NoLableInputProps = {
  style: object;
  type: string;
  placeholer: string;
};

function NoLableInput(props: NoLableInputProps) {
  const [value, setValue] = useState('');
  const handleChange = (event: any) => {
    setValue(event.target.value);
  };
  return (
    <input
      className="no-label-input"
      style={props.style}
      type={props.type}
      value={value}
      onChange={handleChange}
      placeholder={props.placeholer}
    />
  );
}

export default NoLableInput;
