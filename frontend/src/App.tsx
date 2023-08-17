import React, { useState } from 'react';
import { BrowserRouter, Routes, Route } from 'react-router-dom';
import LoginPage from './page/LoginPage/LoginPage';
import SignUpPage from './page/SignUpPage/SignUpPage';
import MyPage from './page/MyPage/MyPage';
import MainPage from './page/MainPage/MainPage';
import WaitingRoomPage from './page/WatingRoomPage/WatingRoomPage';
import MeetingPage from './page/MeetingPage/MeetingPage';
import RatingModal from './components/Modal/RatingModal/RatingModal';
import ResultModal from './components/Modal/ResultModal/ResultModal';
import SubMeetingPage from './page/SubMeetingPage/SubMeetingPage';
import ResultPage from './page/ResultPage/ResultPage';

import HeartCursor from './components/HeartCursor';

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
          <Route path="/sub-meeting/:roomId" element={<SubMeetingPage />}></Route>
          <Route path="/result" element={<ResultPage />}></Route>
          <Route path="/signup" element={<SignUpPage />}></Route>
          <Route path="/mypage" element={<MyPage />}></Route>
          <Route path="/test" element={<ResultModal onClose={handleCloseModal} />} />{' '}
        </Routes>
      </BrowserRouter>
      <HeartCursor />
    </div>
  );
}

export default App;
