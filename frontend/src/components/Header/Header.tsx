import React, { useState } from 'react';
import logo from '../../assets/image/logo.png';
import './Header.css';
import BasicAvartar from '../Avarta/BasicAvatar/BasicAvartar';
import { logout } from '../../app/slices/user';

interface HeaderProps {
  onModalToggle?: () => void;
}

const handleLogout = () => {
  logout(); // 로그아웃 함수를 호출하여 토큰을 삭제하고 Redux 상태를 초기화합니다.
  // 선택적으로, 로그아웃 후 로그인 페이지로 리다이렉트할 수 있습니다.
  window.location.href = '/'; // 로그아웃 후 로그인 페이지로 리다이렉트합니다.
};

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
          <li className="btn">
            <a href="/" onClick={handleLogout}>
              로그아웃
            </a>
          </li>
          <li className="btn">
            <a href="/mypage">마이페이지</a>
          </li>
        </ul>
      </nav>
    </header>
  );
}

export default Header;
