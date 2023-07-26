import React from 'react';
import { BrowserRouter, Routes, Route } from 'react-router-dom';
import LoginPage from './page/LoginPage/LoginPage';
import SignUpPage from './page/SignUpPage/SignUpPage';
import MyPage from './page/MyPage/MyPage';
import HelpModal from './components/Modal/HelpModal/HelpModal';
import ProfileModal from './components/Modal/ProfileModal/ProfileModal';

function App() {
  return (
    <div className="App">
      <BrowserRouter>
        <Routes>
          <Route path="/" element={<LoginPage />}></Route>
          <Route path="/signup" element={<SignUpPage />}></Route>
          <Route path="/mypage" element={<MyPage />}></Route>
          <Route path="/help" element={<HelpModal />}></Route>
          <Route path="/test" element={<ProfileModal />}></Route>
        </Routes>
      </BrowserRouter>
    </div>
  );
}

export default App;
