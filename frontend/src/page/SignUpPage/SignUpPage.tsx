import React, { useState } from 'react';
import Button from '../../components/Button/FilledButton';
import Input from '../../components/Input/NoLabelInput/NoLabelInput';
import IconLabelInput from '../../components/Input/IconLabelInput/IconLabelInput';
import './SignUpPage.css';
import logo from '../../assets/image/logo.png';
import leftSignal from '../../assets/image/left_signal.png';

import { useAppDispatch } from '../../app/hooks';
import { signupAsync } from '../../app/slices/user';

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
  input2: {
    width: '20rem',
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
  const [loginId, setloginId] = useState('');
  const [password, setPassword] = useState('');
  const [confirmPassword, setConfirmPassword] = useState('');
  const [birthdate, setBirthdate] = useState('');
  const [gender, setGender] = useState('');
  const [phone, setPhone] = useState('');
  const [isIdAvailable, setIsIdAvailable] = useState(false); // 중복 체크 결과를 나타내는 상태 변수
  const [passwordMatchMessage, setPasswordMatchMessage] = useState('');

  const handleChangeId = (event: React.ChangeEvent<HTMLInputElement>) => {
    setloginId(event.target.value);
    setIsIdAvailable(false); // 아이디 입력 시 중복 체크 결과를 초기화
  };
  const handleChangePassword = (event: React.ChangeEvent<HTMLInputElement>) => {
    setPassword(event.target.value);
    setPasswordMatchMessage(''); // 비밀번호 변경 시 비밀번호 일치 여부 메시지 초기화
  };
  const handleChangeConfirmPassword = (event: React.ChangeEvent<HTMLInputElement>) => {
    setConfirmPassword(event.target.value);
    setPasswordMatchMessage(''); // 비밀번호 확인 변경 시 비밀번호 일치 여부 메시지 초기화

    // 비밀번호 확인 입력 시 비밀번호와 일치 여부를 확인하여 메시지를 설정합니다.
    if (event.target.value === password) {
      setPasswordMatchMessage('비밀번호가 일치합니다.');
    } else {
      setPasswordMatchMessage('비밀번호가 일치하지 않습니다.');
    }
  };

  const handleChangeBirthdate = (event: React.ChangeEvent<HTMLInputElement>) =>
    setBirthdate(event.target.value);

  const handleGenderSelection = (selectedGender: string) => {
    setGender(selectedGender);
  };

  const handleChangePhone = (event: React.ChangeEvent<HTMLInputElement>) =>
    setPhone(event.target.value);

  const dispatch = useAppDispatch();

  const Signup = async (event: React.MouseEvent<HTMLButtonElement>) => {
    event.preventDefault();

    const data = {
      loginId: loginId,
      password: password,
      confirmPassword: confirmPassword,
      birth: birthdate,
      gender: gender,
      phoneNumber: phone,
    };
    console.log(data);
    try {
      const response = await dispatch(signupAsync(data)); // 회원가입 요청, 서버 응답 처리를 기다립니다.
      console.log('회원가입 성공!');
      console.log('회원 정보:', response.payload); // 회원가입 후 서버로부터 받은 응답 데이터를 출력합니다.
    } catch (error) {
      console.log('회원가입 실패:', error);
    }
  };

  //중복체크 버튼 기능
  const handleMulticheck = () => {
    // 아이디 중복 체크를 서버에서 처리하는 비동기 함수 또는 API 호출을 작성합니다.
    // 이 예제에서는 간단히 하기 위해 setTimeout을 사용하여 1초 후에 중복 여부를 랜덤하게 설정합니다.
    setTimeout(() => {
      const isDuplicate = Math.random() < 0.5; // 랜덤하게 중복 여부를 설정 (50% 확률로 중복된 아이디)
      setIsIdAvailable(isDuplicate); // true: 사용가능한 아이디, false: 중복된 아이디
    }, 1000);
  };

  //인증하기 버튼 기능
  const Certify = (event: React.MouseEvent<HTMLButtonElement>) => {
    event.preventDefault();
  };

  return (
    <div className="signupbox">
      <div className="header">
        <img className="signup-signal" src={leftSignal} alt="시그널 아이콘" />
        <img className="logo" src={logo} alt="다소니 로고 이미지" />
      </div>
      <div className="signup-box">
        <div className="signup-content">
          <div className="signup-id">
            <label htmlFor="label id">아이디</label>
            <Input
              style={styles.input}
              type="text"
              value={loginId}
              handleChange={handleChangeId}
              placeholer="사용하실 아이디를 입력해주세요."
            />
            <div className="button-multicheck">
              <Button style={styles.button2} content="중복체크" handleClick={handleMulticheck} />
            </div>
          </div>
          {/* 현재 사용가능한 아이디(isIdAvailable(true))일 경우만 출력됨 */}
          {isIdAvailable && (
            <div className="id-availability-message">
              {isIdAvailable ? '사용가능한 아이디입니다.' : '중복된 아이디입니다.'}
            </div>
          )}

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
            {/* 비밀번호 일치 여부 메시지 출력 */}
            {passwordMatchMessage && (
              <div className="password-match-message">{passwordMatchMessage}</div>
            )}
          </div>

          <div className="signup-birthdate">
            <div className="birthdate-container">
              <IconLabelInput
                style={styles.input2}
                label="생년월일"
                type="text"
                value={birthdate}
                handleChange={handleChangeBirthdate}
                placeholer="생년월일을 선택하세요"
              />
            </div>
            <input
              className="birth-calendar"
              type="date"
              id="start"
              name="trip-start"
              value={birthdate}
              onChange={handleChangeBirthdate}
              min="1980-01-01"
              max="2005-12-31"
            ></input>
          </div>
          <div className="signup-gender">
            <label htmlFor="label gender">성별</label>
            <div className="gender-container">
              <button
                className={gender === 'male' ? 'gender-man selected' : 'gender-man'}
                onClick={() => handleGenderSelection('male')}
              >
                남
              </button>
              <button
                className={gender === 'female' ? 'gender-woman selected' : 'gender-woman'}
                onClick={() => handleGenderSelection('female')}
              >
                여
              </button>
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
