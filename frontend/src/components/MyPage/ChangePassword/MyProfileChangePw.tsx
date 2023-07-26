import React, { useState } from 'react';
import './MyProfileChangePw.css';
import FiledLabelInput from '../../Input/FilledLabelInput/FilledLabelInput';
import FilledButton from '../../Button/FilledButton';
import axios from 'axios';
import { useAppSelector } from '../../../app/hooks';

const styles = {
  input: {
    height: '3rem',
    flexShrink: '0',
    borderRadius: '1.25rem',
    border: '3px solid #D9D9D9',
    background: '#FFF',
    color: '#898989',
    fontSize: '1rem',
    margin: '0.5rem 0',
    padding: '0.5rem 0.7rem',
  },
  button: {
    borderRadius: '0.625rem',
    background: '#EC5E98',
    width: '7rem',
    height: '3rem',
    position: 'absolute',
    right: 0,
    marginTop: '1rem',
  },
};

// 현재 비밀번호 확인 컴포넌트 :: 이중 인증 목적
function CheckCurrentPw({ setIsCorrect, memberId }: any) {
  const [currentPw, setCurrentPw] = useState('');
  const [alertMsg, setAlertMsg] = useState('');
  const handleCurrentPw = (e: React.ChangeEvent<HTMLInputElement>) => {
    setCurrentPw(e.target.value);
  };
  const handleClick = async () => {
    console.log(currentPw);
    if (!currentPw) {
      setAlertMsg('비밀번호를 입력해주세요');
      return;
    }
    const res = await axios.post(`/users/${memberId}/password`);

    if (res.data) {
      setIsCorrect(true);
      setAlertMsg('');
    }
  };

  return (
    <div className="confirm-currentPw">
      <FiledLabelInput
        style={styles.input}
        label="현재 비밀번호"
        type="password"
        value={currentPw}
        placeholer="현재 비밀번호를 입력해주세요"
        handleChange={handleCurrentPw}
      />
      <div className={`alert ${alertMsg ? 'show' : ''}`}>{alertMsg}</div>
      <FilledButton style={styles.button} content="확인" handleClick={handleClick} />
    </div>
  );
}

// 비밀번호 변경 컴포넌튼
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
    // TODO : 비밀번호 유효성 검사
    console.log(newPw);
    console.log(checkNewPw);

    if (newPw.length < 8 || newPw.length > 10) {
      setAlertMsg('비밀번호를 8자 이상 10자 이하로 작성해주세요');
      return;
    } else if (newPw !== checkNewPw) {
      setAlertMsg('비밀번호가 일치하지 않습니다. 다시 확인해주세요');
      return;
    }

    const res = await axios.post(`/users/${memberId}/password`);
    console.log(res.data);

    if (res.data === 'OK') {
      alert(`비밀번호가 성공적으로 변경되었습니다.`);
      setAlertMsg('');
      setType('read');
    }
  };

  return (
    <div className="confirm-changePw">
      <FiledLabelInput
        style={styles.input}
        label="변경 비밀번호"
        type="password"
        value={newPw}
        placeholer="변경할 비밀번호를 입력해주세요"
        handleChange={handleNewPwChange}
      />
      <FiledLabelInput
        style={styles.input}
        label="비밀번호 확인"
        type="password"
        value={checkNewPw}
        placeholer="다시 한 번 비밀번호를 입력해주세요"
        handleChange={handleCheckNewPwChange}
      />
      <div className={`alert ${alertMsg ? 'show' : ''}`}>{alertMsg}</div>
      <FilledButton style={styles.button} content="확인" handleClick={sendNewPw} />
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
        <CheckCurrentPw setIsCorrect={setIsCorrect} memberId={memberId} />
      ) : (
        <ChangePw setType={setType} memberId={memberId} />
      )}
    </div>
  );
}

export default MyProfileChangePw;
