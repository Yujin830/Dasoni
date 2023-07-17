import React from 'react';
import './Button.css';

type FilledButtonProps = {
  style: object;
  content: string;
};

function FilledButton(props: FilledButtonProps) {
  return (
    <button className="btn" style={props.style}>
      {props.content}
    </button>
  );
}

export default FilledButton;
