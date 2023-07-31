import React, { useState } from 'react';
import Button from '../../components/Button/FilledButton';
import Input from '../../components/Input/NoLabelInput/NoLabelInput';
import './LoginPage.css';
import logo from '../../assets/image/logo.png';
import main from '../../assets/image/main_img.jpg';
import leftSignal from '../../assets/image/left_signal.png';
import rightSignal from '../../assets/image/right_signal.png';
import { useAppDispatch } from '../../app/hooks';
import { setUserAsync, getUserInfo } from '../../app/slices/user';
import { Link } from 'react-router-dom';

const styles = {
  button: {
    width: '26rem',
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

function LoginPage() {
  const [id, setId] = useState('');
  const [password, setPassword] = useState('');

  const handleChangeId = (event: React.ChangeEvent<HTMLInputElement>) => {
    setId(event.target.value);
  };

  const handleChangeIdPassword = (event: React.ChangeEvent<HTMLInputElement>) => {
    setPassword(event.target.value);
  };

  const dispatch = useAppDispatch();
  const Login = (event: React.MouseEvent<HTMLButtonElement>) => {
    event.preventDefault();
    const data = {
      id: id,
      password: password,
    };
    console.log('login');
    dispatch(setUserAsync(data));
    console.log(data);
  };

  return (
    <div className="box" id="login">
      <div className="left-box">
        <img className="signal left" src={leftSignal} alt="시그널 아이콘" />
        <div className="left-box-content">
          <div className="title">
            <h2>두근두근</h2>
            <h3>모르는 사람들과의 랜덤 미팅</h3>
          </div>
          <img src={main} alt="있어보이는 우리와 어울리는 사진" />
        </div>
      </div>
      <div className="right-box">
        <img className="signal right" src={rightSignal} alt="시그널 아이콘" />
        <div className="right-box-content">
          <img src={logo} alt="다소니 로고 이미지" />
          <form>
            <Input
              style={styles.input}
              type="text"
              value={id}
              handleChange={handleChangeId}
              placeholer="아이디를 입력해주세요."
            />
            <Input
              style={styles.input}
              type="password"
              value={password}
              handleChange={handleChangeIdPassword}
              placeholer="비밀번호를 입력해주세요."
            />
            <Button style={styles.button} content="로그인" handleClick={Login} />
          </form>
          <p>
            아직 회원이 아니신가요?{' '}
            <Link to="/signup" id="regist">
              가입하기
            </Link>
          </p>
        </div>
      </div>
    </div>
  );
}

export default LoginPage;
