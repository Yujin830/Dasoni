import React from 'react';
import './Button.css';

type IconButtonProps = {
  style: object;
  content: string;
  iconPosition: string; //left | right
  icon: string;
  handleClick: (event: React.MouseEvent<HTMLButtonElement>) => void;
};

function IconButton({ style, content, iconPosition, icon, handleClick }: IconButtonProps) {
  return (
    <button className="btn" style={style} onClick={handleClick}>
      {iconPosition === 'left' ? (
        <span className="content-box">
          <span className="material-symbols-outlined btn-icon">{icon}</span> {content}
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
