import React, { useState } from 'react';
import Button from '../../components/Button/FilledButton';
import Input from '../../components/Input/NoLabelInput/NoLabelInput';
// import IconLabelInput from '../../components/Input/IconLabelInput/IconLabelInput';
import './SignUpPage.css';
import logo from '../../assets/image/logo.png';
import leftSignal from '../../assets/image/left_signal.png';

import { useAppDispatch } from '../../app/hooks';
import { signupAsync } from '../../app/slices/user';
import { useNavigate } from 'react-router-dom';
import axios from 'axios';

const styles = {
  button: {
    width: '100%',
    height: '100%',
    flexShrink: '0',
    borderRadius: '1.25rem',
    background: '#EC5E98',
    color: '#FFF',
    fontSize: '2.5vh',
    fontStyle: 'normal',
    fontWeight: '700',
    margin: '0.5rem 0',
  },
  button2: {
    width: '100%',
    height: '100%',
    flexshrink: '0',
    borderRadius: '1rem',
    background: '#FFE8EF',
    color: '#555',
    fontSize: '2vh',
    fontStyle: 'normal',
    fontWeight: '700',
    // margin: '0.5rem 0',
  },
  input: {
    width: '60%',
    height: '80%',
    flexShrink: '0',
    borderRadius: '1.25rem',
    border: '3px solid #D9D9D9',
    background: '#FFF',
    color: '#898989',
    fontSize: '1rem',
    // margin: '0.5rem 0',
    padding: '0.5rem 0.7rem',
  },
  input2: {
    width: '40%',
    height: '80%',
    flexShrink: '0',
    borderRadius: '1.25rem',
    border: '3px solid #D9D9D9',
    background: '#FFF',
    color: '#898989',
    fontSize: '1rem',
    // margin: '0.5rem 0',
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
  const [passwordMatchMessage, setPasswordMatchMessage] = useState('');
  const [isIdAvailable, setIsIdAvailable] = useState(true);
  const [MulticheckClicked, setMulticheckClicked] = useState(false);
  const navigate = useNavigate();

  const handleChangeId = (event: React.ChangeEvent<HTMLInputElement>) => {
    const newloginId = event.target.value;
    setloginId(newloginId);

    // loginId가 변경되면 isIdAvailable을 true로 초기화
    if (newloginId !== '') {
      setIsIdAvailable(true);
      setMulticheckClicked(false);
    }
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
      navigate('/');
    } catch (error) {
      console.log('회원가입 실패:', error);
    }
  };

  //중복체크 버튼 기능
  const handleMulticheck = async () => {
    // 로그인 아이디가 비어있는 경우 중복 체크를 하지 않음
    if (!loginId) {
      return;
    }

    try {
      // 서버로 로그인 아이디 중복 체크 요청을 보냄
      const response = await axios.post(`/api/register/${loginId}`);

      console.log('중복체크 성공');
      console.log(response.status);

      if (response.status === 200) {
        // 중복된 아이디인 경우
        setIsIdAvailable(false);
      } else if (response.status === 401) {
        // 중복되지 않은 아이디인 경우
        setIsIdAvailable(true);
      }
    } catch (error) {
      console.log(error);
      // 401에러로 인식함 -> 중복되지 않은 아이디
      setIsIdAvailable(true);
    }
    // 중복 체크 버튼이 클릭되었음을 표시
    setMulticheckClicked(true);
  };
  //인증하기 버튼 기능
  const Certify = (event: React.MouseEvent<HTMLButtonElement>) => {
    event.preventDefault();
  };

  return (
    <div className="signupbox">
      <div className="header">
        <img className="signup-signal" src={leftSignal} alt="시그널 아이콘" />
        <img className="signup-logo" src={logo} alt="다소니 로고 이미지" />
      </div>
      <div className="signup-box">
        <div className="signup-content">
          <div className="signup-id">
            {/* <label htmlFor="label id">아이디</label> */}
            <div className="label-name">아이디</div>
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
          <div className="msg">
            {/* MulticheckClicked가 true일 때에만 메시지를 표시 */}
            {MulticheckClicked && isIdAvailable !== null && (
              <div className="id-availability-message">
                {isIdAvailable ? '사용 가능한 아이디입니다.' : '중복된 아이디입니다.'}
              </div>
            )}
          </div>
          <div className="signup-password">
            {/* <label htmlFor="label password">비밀번호</label> */}
            <div className="label-name">비밀번호</div>
            <Input
              style={styles.input}
              type="password"
              value={password}
              handleChange={handleChangePassword}
              placeholer="사용하실 비밀번호를 입력해주세요."
            />
          </div>
          <div className="signup-confirmedpassword">
            {/* <label htmlFor="label confirmedpassword">비밀번호 확인</label> */}
            <div className="label-name">비밀번호 확인</div>
            <Input
              style={styles.input}
              type="password"
              value={confirmPassword}
              handleChange={handleChangeConfirmPassword}
              placeholer="비밀번호를 다시한번 입력해주세요"
            />
          </div>
          <div className="msg">
            {/* 비밀번호 일치 여부 메시지 출력 */}
            {passwordMatchMessage && (
              <div className="password-match-message">{passwordMatchMessage}</div>
            )}
          </div>
          <div className="signup-birthdate">
            {/* <div className="birthdate-container"> */}
            {/* <label htmlFor="label birth">생년월일</label> */}
            <div className="label-name">생년월일</div>
            <Input
              style={styles.input2}
              type="text"
              value={birthdate}
              handleChange={handleChangeBirthdate}
              placeholer="생년월일을 선택하세요"
            />
            {/* </div> */}
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
            {/* <label htmlFor="label gender">성별</label> */}
            <div className="label-name">성별</div>
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
            {/* <label htmlFor="label phone">전화번호</label> */}
            <div className="label-name">전화번호</div>
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
          <div className="btn">
            <Button style={styles.button} content="가입하기" handleClick={Signup} />
          </div>
        </div>
      </div>
    </div>
  );
}

export default SignupPage;
