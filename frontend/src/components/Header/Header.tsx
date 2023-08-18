import React, { useState } from 'react';
import logo from '../../assets/image/logo.png';
import './Header.css';
import BasicAvartar from '../Avarta/BasicAvatar/BasicAvartar';
import { logout, setProfileImageSrc } from '../../app/slices/user';
import { Link } from 'react-router-dom';
import { useNavigate } from 'react-router-dom';
import heartRating from '../../assets/image/heart/heart_rating.png';
import { useAppSelector } from '../../app/hooks';
import RankAvartar from '../../components/Avarta/RankAvartar/RackAvartar';
import ExpPointBar from '../../components/Element/ExpPointBar';
import { useDispatch } from 'react-redux';
import convertScoreToPercent from '../../utils/convertScoreToPercent';

interface HeaderProps {
  onModalToggle?: () => void;
}

function Header({ onModalToggle }: HeaderProps) {
  const navigate = useNavigate();
  const { remainLife } = useAppSelector((state) => state.user);
  const dispatch = useDispatch();

  const handleLogout = () => {
    logout(); // 로그아웃 함수를 호출하여 토큰을 삭제하고 Redux 상태를 초기화합니다.
    navigate('/');
  };
  // 메뉴 버튼 토클
  const [isOpen, setIsOpen] = useState(false);
  const handleToggleMenu = () => {
    setIsOpen((prevState) => !prevState);
  };
  const handleHelpClick = () => {
    if (onModalToggle) {
      onModalToggle();
    }
    setIsOpen(false);
  };

  // 라이프 UI 생성
  const drawLife = () => {
    const lives = [];
    if (remainLife !== undefined) {
      const spendLife = 2 - remainLife;
      // 소비한 목숨 draw
      for (let i = 0; i < spendLife; i++) {
        lives.push(
          <span key={`spend-${i}`} className="material-symbols-outlined">
            favorite
          </span>,
        );
      }

      // 남은 목숨 draw
      for (let i = 0; i < remainLife; i++) {
        lives.push(
          <span key={`remain-${i}`} className="material-symbols-outlined filled">
            favorite
          </span>,
        );
      }
    }

    return lives;
  };

  //사이드바 토글
  const [sideOpen, setSideopen] = useState(false);
  const ToggleSidebar = () => {
    sideOpen === true ? setSideopen(false) : setSideopen(true);
  };

  // 사이드바 데이터 반영
  const { rating, profileImageSrc } = useAppSelector((state) => state.user);

  return (
    <header className="header">
      <button className="material-symbols-outlined header-mobile" onClick={ToggleSidebar}>
        double_arrow
      </button>
      <Link className="logo" to="/main">
        <img src={logo} alt="다소니 로고 이미지"></img>
      </Link>
      <nav className="nav">
        <ul id="nav-bar">
          <li>{drawLife()}</li>
          {/* <li>
            <BasicAvartar src={imagedefault} />
          </li> */}

          <div id="filter-menu">
            <button className="material-symbols-outlined" onClick={handleToggleMenu}>
              menu
            </button>
            <ul className={isOpen ? 'show' : ''}>
              <li id="help">
                <button className="material-symbols-outlined" onClick={handleHelpClick}>
                  help도움말
                </button>
              </li>
              <li id="mypage">
                <Link to="/mypage">마이페이지</Link>
              </li>
              <li id="logout">
                <button onClick={handleLogout}>로그아웃</button>
              </li>
            </ul>
          </div>
        </ul>
      </nav>
      <div>
        <div className={`sidebar ${sideOpen == true ? 'active' : ''}`}>
          <div className="sd-header">
            <button className="material-symbols-outlined header-mobile" onClick={ToggleSidebar}>
              keyboard_double_arrow_left
            </button>
          </div>
          <div className="sd-body">
            {/* <div className="sidebar-profile">
              <img src={profileImageSrc} alt="프로필 이미지" />
            </div>
            <div className="sidebar-rating">My Rating</div> */}
            <div className="sidebar-profile">
              <div className="profile-container">
                <RankAvartar profileSrc={profileImageSrc} point={rating} />
              </div>
            </div>

            <div className="sidebar-rating">
              {/* <p className="title"> Signal</p> */}
              <ExpPointBar percent={convertScoreToPercent(rating || 0)} points={rating} />
            </div>
            <div className="sidebar-heart">
              <h2>Rating Info</h2>
              <img className="heart-rating" src={heartRating} alt="하트 등급" />
            </div>
          </div>
        </div>
        <div className={`sidebar-overlay ${sideOpen == true ? 'active' : ''}`}></div>
      </div>
    </header>
  );
}

export default Header;
