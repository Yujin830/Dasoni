import React, { useState } from 'react';
import './MyProfileChangePw.css';
import FiledLabelInput from '../../Input/FilledLabelInput/FilledLabelInput';
import FilledButton from '../../Button/FilledButton';
import axios from 'axios';
import { useAppSelector } from '../../../app/hooks';

// 현재 비밀번호 확인 컴포넌트 :: 이중 인증 목적
function CheckCurrentPw({ setIsCorrect, memberId, setType }: any) {
  const [currentPw, setCurrentPw] = useState('');
  const [alertMsg, setAlertMsg] = useState('');
  const handleCurrentPw = (e: React.ChangeEvent<HTMLInputElement>) => {
    setCurrentPw(e.target.value);
  };
  const cancelChangePw = (e: React.MouseEvent<HTMLButtonElement>) => {
    e.preventDefault();
    setType('read');
  };
  const handleClick = async () => {
    console.log(currentPw);
    if (!currentPw) {
      setAlertMsg('비밀번호를 입력해주세요');
      return;
    }

    try {
      const res = await axios.post(`/api/users/${memberId}/password`, { password: currentPw });
      console.log('data' + res.data);
      if (res.data) {
        setIsCorrect(true);
        setAlertMsg('');
      }
    } catch (err) {
      console.log(err);
      setAlertMsg('비밀번호가 틀렸습니다. 다시 입력해주세요');
    }
  };

  return (
    <div className="confirm-currentPw">
      <div className="current-container">
        <FiledLabelInput
          classes="confirm-input"
          label="현재 비밀번호"
          type="password"
          value={currentPw}
          placeholer="현재 비밀번호를 입력해주세요"
          handleChange={handleCurrentPw}
        />
        <div className={`alert ${alertMsg ? 'show' : ''}`}>{alertMsg}</div>
      </div>

      <div className="c-btn">
        <FilledButton classes="cancel-btn" content="취소" handleClick={cancelChangePw} />
        <FilledButton classes="confirm-btn" content="확인" handleClick={handleClick} />
      </div>
    </div>
  );
}

// 비밀번호 변경 컴포넌트
function ChangePw({ setType, memberId }: any) {
  const [newPw, setNewPw] = useState('');
  const [checkNewPw, setCheckNewPw] = useState('');
  const [alertMsg, setAlertMsg] = useState('');

  const handleNewPwChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setNewPw(e.target.value);
  };

  const handleCheckNewPwChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setCheckNewPw(e.target.value);
  };

  const sendNewPw = async () => {
    console.log(newPw);
    console.log(checkNewPw);

    if (newPw.length < 8 || newPw.length > 10) {
      setAlertMsg('비밀번호를 8자 이상 10자 이하로 작성해주세요');
      return;
    } else if (newPw !== checkNewPw) {
      setAlertMsg('비밀번호가 일치하지 않습니다. 다시 확인해주세요');
      return;
    }

    try {
      const res = await axios.patch(`/api/users/${memberId}/password`, { password: newPw });
      console.log(res.data);

      if (res.data === 'OK') {
        alert(`비밀번호가 성공적으로 변경되었습니다.`);
        setAlertMsg('');
        setType('read');
      }
    } catch (err) {
      console.log(err);
    }
  };

  return (
    <div className="confirm-changePw">
      <div className="confirm-container">
        <FiledLabelInput
          classes="confirm-input"
          label="변경 비밀번호"
          type="password"
          value={newPw}
          placeholer="변경할 비밀번호를 입력해주세요"
          handleChange={handleNewPwChange}
        />
        <FiledLabelInput
          classes="confirm-input"
          label="비밀번호 확인"
          type="password"
          value={checkNewPw}
          placeholer="다시 한 번 비밀번호를 입력해주세요"
          handleChange={handleCheckNewPwChange}
        />
        <div className={`alert ${alertMsg ? 'show' : ''}`}>{alertMsg}</div>
      </div>
      <FilledButton classes="confirm-btn" content="확인" handleClick={sendNewPw} />
    </div>
  );
}

function MyProfileChangePw({ setType }: any) {
  const [isCorrect, setIsCorrect] = useState(false);
  const { memberId } = useAppSelector((state) => state.user);

  return (
    <div className="content changePw-box">
      <h3>비밀번호 변경</h3>
      {!isCorrect ? (
        <CheckCurrentPw setType={setType} setIsCorrect={setIsCorrect} memberId={memberId} />
      ) : (
        <ChangePw setType={setType} memberId={memberId} />
      )}
    </div>
  );
}

export default MyProfileChangePw;
