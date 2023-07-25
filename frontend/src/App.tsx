import React from 'react';
import { BrowserRouter, Routes, Route } from 'react-router-dom';
import LoginPage from './page/LoginPage/LoginPage';
import SignUpPage from './page/SignUpPage/SignUpPage';
import MyPage from './page/MyPage/MyPage';

function App() {
  return (
    <div className="App">
      <BrowserRouter>
        <Routes>
          <Route path="/" element={<LoginPage />}></Route>
          <Route path="/signup" element={<SignUpPage />}></Route>
          <Route path="/mypage" element={<MyPage />}></Route>
        </Routes>
      </BrowserRouter>
    </div>
  );
}

export default App;
