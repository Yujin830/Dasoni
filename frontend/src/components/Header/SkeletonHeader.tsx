import React from 'react';

const SkeletonHeader = () => {
  return (
    <header className="header">
      <button className="material-symbols-outlined">double_arrow</button>

      <nav className="nav">
        <ul id="nav-bar">
          <li>
            <span className="material-symbols-outlined filled">favorite</span>
            <span className="material-symbols-outlined filled">favorite</span>
          </li>

          <div id="filter-menu"></div>
        </ul>
      </nav>
    </header>
  );
};

export default SkeletonHeader;
