import React from 'react';
import './Button.css';

type FilledButtonProps = {
  classes?: string;
  style?: object;
  content: string;
  handleClick: (event: React.MouseEvent<HTMLButtonElement>) => void;
};

function FilledButton({ style, content, handleClick, classes }: FilledButtonProps) {
  return (
    <button className={`btn ${classes}`} style={style} onClick={handleClick}>
      {content}
    </button>
  );
}

export default FilledButton;
