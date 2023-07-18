import React, { useState } from 'react';
import './NoLabelInput.css';

type NoLableInputProps = {
  style: object;
  type: string;
  value: string;
  handleChange: any;
  placeholer: string;
};

function NoLableInput(props: NoLableInputProps) {
  return (
    <input
      className="no-label-input"
      style={props.style}
      type={props.type}
      value={props.value}
      onChange={props.handleChange}
      placeholder={props.placeholer}
    />
  );
}

export default NoLableInput;
