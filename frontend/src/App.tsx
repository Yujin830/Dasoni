import React, { useState } from 'react';
import { BrowserRouter, Routes, Route } from 'react-router-dom';
import LoginPage from './page/LoginPage/LoginPage';
import SignUpPage from './page/SignUpPage/SignUpPage';
import MyPage from './page/MyPage/MyPage';

import HelpModal from './components/Modal/HelpModal/HelpModal';
import ProfileModal from './components/Modal/ProfileModal/ProfileModal';
import MainPage from './page/MainPage/MainPage';
import WaitingRoomPage from './page/WatingRoomPage/WatingRoomPage';

import OpenRoomModal from './components/Modal/OpenRoomModal/OpenRoomModal';
import MeetingPage from './page/MeetingPage/MeetingPage';

function App() {
  const [isModalOpen, setModalOpen] = useState(false);

  const handleCloseModal = () => {
    // 모달을 닫는 동작을 여기에 구현
    setModalOpen(false);
  };
  return (
    <div className="App">
      <BrowserRouter>
        <Routes>
          <Route path="/" element={<LoginPage />}></Route>
          <Route path="/main" element={<MainPage />}></Route>
          <Route path="/waiting-room/:roomId" element={<WaitingRoomPage />}></Route>
          <Route path="/meeting/:roomId" element={<MeetingPage />}></Route>
          <Route path="/signup" element={<SignUpPage />}></Route>
          <Route path="/mypage" element={<MyPage />}></Route>
          <Route path="/test" element={<OpenRoomModal onClose={handleCloseModal} />} />{' '}
        </Routes>
      </BrowserRouter>
    </div>
  );
}

export default App;
