import React from 'react';
import './Button.css';

type FilledButtonProps = {
  classes?: string;
  style?: object;
  content: string;
  handleClick: (event: React.MouseEvent<HTMLButtonElement>) => void;
  disabled?: boolean;
};

function FilledButton({ style, content, handleClick, classes, disabled }: FilledButtonProps) {
  return (
    <button className={`btn ${classes}`} style={style} onClick={handleClick}>
      {content}
    </button>
  );
}

export default FilledButton;
