import React from 'react';
import './Button.css';

type FilledButtonProps = {
  style: object;
  content: string;
  handleClick: any;
};

function FilledButton(props: FilledButtonProps) {
  return (
    <button className="btn" style={props.style} onClick={props.handleClick}>
      {props.content}
    </button>
  );
}

export default FilledButton;
