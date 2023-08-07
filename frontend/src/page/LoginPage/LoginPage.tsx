import React, { useState } from 'react';
import { RootState } from '../../app/store';
import { useAppSelector } from '../../app/hooks';
import Button from '../../components/Button/FilledButton';
import Input from '../../components/Input/NoLabelInput/NoLabelInput';
import './LoginPage.css';
import logo from '../../assets/image/logo.png';
import main from '../../assets/image/main_img.png';
import leftSignal from '../../assets/image/left_signal.png';
import rightSignal from '../../assets/image/right_signal.png';
import { useAppDispatch } from '../../app/hooks';
import { loginAsync, getUserInfo } from '../../app/slices/user';
import { Link } from 'react-router-dom';
import ProfileModal from '../../components/Modal/ProfileModal/ProfileModal';
import { useNavigate } from 'react-router-dom';
import { useNavigate } from 'react-router-dom';

const styles = {
  button: {
    width: '40%',
    height: '6vh',
    flexShrink: '0',
    borderRadius: '1.25rem',
    background: '#EC5E98',
    color: '#FFF',
    fontSize: '3vh',
    fontStyle: 'normal',
    fontWeight: '700',
    margin: '0.5rem 0',
  },
  input: {
    width: '80%',
    height: '8vh',
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
  const [loginId, setloginId] = useState('');
  const [password, setPassword] = useState('');
  const [isModalOpen, setModalOpen] = useState(false);
  const navigate = useNavigate();
  const { isFirst } = useAppSelector((state) => state.user);
  const navigate = useNavigate();

  const handleChangeId = (event: React.ChangeEvent<HTMLInputElement>) => {
    setloginId(event.target.value);
  };

  const handleChangeIdPassword = (event: React.ChangeEvent<HTMLInputElement>) => {
    setPassword(event.target.value);
  };

  const dispatch = useAppDispatch();

  const handleLogin = async (event: React.MouseEvent<HTMLButtonElement>) => {
    event.preventDefault();

    const data = {
      loginId: loginId,
      password: password,
    };

    try {
      const response: any = await dispatch(loginAsync(data));
      if (response.payload) {
        console.log('로그인 성공!');
        alert('로그인되었습니다.');
        if (response.payload.isFirst === 1) {
          setModalOpen(true);
        } else {
          setModalOpen(false);
          navigate('/main'); // 두번째 로그인부터는 바로 메인으로
          navigate('/main');
        }
      } else {
        console.log('로그인 실패');
        alert('올바르지 않은 아이디 혹은 비밀번호입니다.');
        setModalOpen(false); // 로그인에 실패하면 모달 닫기
      }
    } catch (error) {
      console.log('로그인 실패:', error);
      setModalOpen(false);
    }
  };

  return (
    <div className="box">
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
              value={loginId}
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
            <Button style={styles.button} content="로그인" handleClick={handleLogin} />
            {/* isModalOpen 변수에 따라 ProfileModal 컴포넌트를 조건부 렌더링합니다 */}
            {isModalOpen && <ProfileModal onClose={() => setModalOpen(false)} />}
          </form>
          <p>
            아직 회원이 아니신가요?{' '}
            <Link to="/signup" id="regist">
              가입하기
            </Link>
          </p>
        </div>
        <img className="mobile" src={main} alt="있어보이는 우리와 어울리는 사진" />
      </div>
    </div>
  );
}

export default LoginPage;
