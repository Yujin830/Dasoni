import React, { useState } from 'react';
import logo from '../../assets/image/logo.png';
import './Header.css';
import BasicAvartar from '../Avarta/BasicAvatar/BasicAvartar';
import HelpModal from '../Modal/HelpModal/HelpModal';

interface HeaderProps {
  onModalToggle: () => void;
}

function Header({ onModalToggle }: HeaderProps) {
  return (
    <header className="header">
      <a className="logo" href="/">
        <img src={logo} alt="다소니 로고 이미지"></img>
      </a>
      <nav className="nav">
        <ul>
          <li>
            <span className="material-symbols-outlined filled">favorite</span>
            <span className="material-symbols-outlined filled">favorite</span>
          </li>

          <li className="help">
            <button
              className="material-symbols-outlined"
              onClick={onModalToggle} // MainPage의 handleModalToggle 함수를 호출
            >
              help
            </button>
          </li>

          <li>
            <BasicAvartar src="default_profile.png" />
          </li>
          <li className="btn">로그아웃</li>
          <li className="btn">
            <a href="/">마이페이지</a>
          </li>
        </ul>
      </nav>
    </header>
  );
}

export default Header;
