import React, { useState } from 'react';
import logo from '../../assets/image/logo.png';
import './Header.css';
import BasicAvartar from '../Avarta/BasicAvatar/BasicAvartar';
import { logout } from '../../app/slices/user';
import { Link } from 'react-router-dom';
import { useNavigate } from 'react-router-dom';

interface HeaderProps {
  onModalToggle?: () => void;
}

function Header({ onModalToggle }: HeaderProps) {
  const navigate = useNavigate();
  const handleLogout = () => {
    logout(); // 로그아웃 함수를 호출하여 토큰을 삭제하고 Redux 상태를 초기화합니다.
    navigate('/');
  };
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
            <button onClick={handleLogout}>로그아웃</button>
          </li>
          <li className="btn">
            <Link to="/mypage">마이페이지</Link>
          </li>
        </ul>
      </nav>
    </header>
  );
}

export default Header;
