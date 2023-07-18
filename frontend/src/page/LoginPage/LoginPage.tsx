import React from 'react';
import Button from '../../components/Button/FilledButton';
import Input from '../../components/NoLabelInput/NoLabelInput';
import './LoginPage.css';

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
  return (
    <div className="box">
      <div className="left-box">
        <h2>두근두근</h2>
      </div>
      <div className="right-box">
        <Input style={styles.input} type="text" placeholer="아이디를 입력해주세요." />
        <Input style={styles.input} type="password" placeholer="비밀번호를 입력해주세요." />
        <Button style={styles.button} content="로그인" />
      </div>
    </div>
  );
}

export default LoginPage;
