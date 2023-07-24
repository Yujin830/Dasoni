import React, { useState } from 'react';
import Button from '../../components/Button/FilledButton';
import Input from '../../components/Input/NoLabelInput/NoLabelInput';
import IconLabelInput from '../../components/Input/IconLabelInput/IconLabelInput';
import './SignUpPage.css';
import logo from '../../assets/image/logo.png';
import leftSignal from '../../assets/image/left_signal.png';

import { useAppDispatch } from '../../app/hooks';
import { setUserAsync } from '../../app/modules/user';

const styles = {
  button: {
    width: '12rem',
    height: '4rem',
    flexShrink: '0',
    borderRadius: '1.25rem',
    background: '#EC5E98',
    color: '#FFF',
    fontSize: '1.75rem',
    fontStyle: 'normal',
    fontWeight: '700',
    margin: '0.5rem 0',
  },
  button2: {
    width: '7.5rem',
    height: '4rem',
    flexshrink: '0',
    borderRadius: '6.25rem',
    background: '#FFE8EF',
    color: '#555',
    fontSize: '1.5rem',
    fontStyle: 'normal',
    fontWeight: '700',
    margin: '0.5rem 0',
  },
  input: {
    width: '26rem',
    height: '4rem',
    flexShrink: '0',
    borderRadius: '1.25rem',
    border: '3px solid #D9D9D9',
    background: '#FFF',
    color: '#898989',
    fontSize: '1rem',
    margin: '0.5rem 0',
    padding: '0.5rem 0.7rem',
  },
};

function SignupPage() {
  const [id, setId] = useState('');
  const [password, setPassword] = useState('');
  const [confirmPassword, setConfirmPassword] = useState('');
  const [birthdate, setBirthdate] = useState('');
  const [gender, setGender] = useState('');
  const [phone, setPhone] = useState('');

  const handleChangeId = (event: React.ChangeEvent<HTMLInputElement>) => {
    setId(event.target.value);
  };
  const handleChangePassword = (event: React.ChangeEvent<HTMLInputElement>) => {
    setPassword(event.target.value);
  };
  const handleChangeConfirmPassword = (event: React.ChangeEvent<HTMLInputElement>) => {
    setConfirmPassword(event.target.value);
  };
  const handleChangeBirthdate = (event: React.ChangeEvent<HTMLInputElement>) =>
    setBirthdate(event.target.value);
  const handleChangeGender = (event: React.ChangeEvent<HTMLInputElement>) =>
    setGender(event.target.value);
  const handleChangePhone = (event: React.ChangeEvent<HTMLInputElement>) =>
    setPhone(event.target.value);

  const dispatch = useAppDispatch();

  const Signup = (event: React.MouseEvent<HTMLButtonElement>) => {
    event.preventDefault();
    const data = {
      id: id,
      password: password,
      confirmPassword: confirmPassword,
      birth: birthdate,
      gender: gender,
      phone: phone,
    };
    console.log('signup');
    // 회원가입을 위한 비동기 액션을 dispatch하도록 액션 함수 작성
    dispatch(setUserAsync(data));
    console.log(data);
  };
  //중복체크 버튼 기능
  const Multicheck = (event: React.MouseEvent<HTMLButtonElement>) => {
    event.preventDefault();
  };
  //인증하기 버튼 기능
  const Certify = (event: React.MouseEvent<HTMLButtonElement>) => {
    event.preventDefault();
  };
  return (
    <div className="signupbox">
      <div className="header">
        <img className="signal left" src={leftSignal} alt="시그널 아이콘" />
        <img className="logo" src={logo} alt="다소니 로고 이미지" />
      </div>
      <div className="signup-box">
        <div className="signup-content">
          <div className="signup-id">
            <label htmlFor="label id">아이디</label>
            <Input
              style={styles.input}
              type="text"
              value={id}
              handleChange={handleChangeId}
              placeholer="사용하실 아이디를 입력해주세요."
            />
            <div className="button-multicheck">
              <Button style={styles.button2} content="중복체크" handleClick={Multicheck} />
            </div>
          </div>
          <div className="signup-password">
            <label htmlFor="label password">비밀번호</label>
            <Input
              style={styles.input}
              type="password"
              value={password}
              handleChange={handleChangePassword}
              placeholer="사용하실 비밀번호를 입력해주세요."
            />
          </div>
          <div className="signup-confirmedpassword">
            <label htmlFor="label confirmedpassword">비밀번호 확인</label>
            <Input
              style={styles.input}
              type="password"
              value={confirmPassword}
              handleChange={handleChangeConfirmPassword}
              placeholer="비밀번호를 다시한번 입력해주세요"
            />
          </div>
          <div className="signup-birthdate">
            {/* <label htmlFor="label birthdate">생년월일</label> */}
            <IconLabelInput
              style={styles.input}
              label="생년월일"
              icon="calendar_month"
              type="text"
              value={birthdate}
              handleChange={handleChangeBirthdate}
              placeholer="생년월일을 선택하세요"
            />
          </div>
          <div className="signup-gender">
            <label htmlFor="label gender">성별</label>
            <div className="gender-container">
              <span className="gender-man">남</span>
              <span className="gender-woman">여</span>
            </div>
          </div>
          <div className="signup-phone">
            <label htmlFor="label phone">전화번호</label>
            <Input
              style={styles.input}
              type="text"
              value={phone}
              handleChange={handleChangePhone}
              placeholer="000-0000-0000"
            />
            <div className="button-certify">
              <Button style={styles.button2} content="인증하기" handleClick={Certify} />
            </div>
          </div>
        </div>
        <div className="button-signup">
          <Button style={styles.button} content="가입하기" handleClick={Signup} />
        </div>
      </div>
    </div>
  );
}

export default SignupPage;
