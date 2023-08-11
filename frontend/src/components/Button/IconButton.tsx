import React from 'react';
import './Button.css';

type IconButtonProps = {
  classes?: string;
  style?: object;
  content: string;
  iconPosition: string; //left | right
  icon: string;
  handleClick: (event: React.MouseEvent<HTMLButtonElement>) => void;
};

function IconButton({ style, content, iconPosition, icon, handleClick, classes }: IconButtonProps) {
  return (
    <button className={`btn ${classes}`} style={style} onClick={handleClick}>
      {iconPosition === 'left' ? (
        <span className="content-box">
          <span className="material-symbols-outlined">{icon}</span> {content}
        </span>
      ) : (
        <span className="content-box">
          {content} <span className="material-symbols-outlined">{icon}</span>
        </span>
      )}
    </button>
  );
}

export default IconButton;
