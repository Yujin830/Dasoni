import React from 'react';
import './Button.css';

type FilledButtonProps = {
  style: object;
  content: string;
  handleClick: (event: React.MouseEvent<HTMLButtonElement>) => void;
};

function FilledButton({ style, content, handleClick }: FilledButtonProps) {
  return (
    <button className="btn" style={style} onClick={handleClick}>
      {content}
    </button>
  );
}

export default FilledButton;
