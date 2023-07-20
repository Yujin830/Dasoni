import React from 'react';
import './Button.css';

type IconButtonProps = {
  style: object;
  content: string;
  iconPosition: string;
  icon: string;
  handleClick: (event: React.MouseEvent<HTMLButtonElement>) => void;
};

function IconButton({ style, content, iconPosition, icon, handleClick }: IconButtonProps) {
  return (
    <button className="btn" style={style} onClick={handleClick}>
      {iconPosition === 'left' ? (
        <span>
          <span className="material-symbols-outlined">{icon}</span> {content}
        </span>
      ) : (
        <span>
          {content} <span className="material-symbols-outlined">{icon}</span>
        </span>
      )}
    </button>
  );
}

export default IconButton;
