import React from 'react';
import Button from '../../components/Button/FilledButton';
import './LoginPage.css';

const styles = {
  width: '26.5rem',
  height: '4.375rem',
  flexShrink: '0',
  borderRadius: '1.25rem',
  background: '#EC5E98',
  color: '#FFF',
  fontSize: '1.75rem',
  fontStyle: 'normal',
  fontWeight: '700',
};

function LoginPage() {
  return (
    <div className="box">
      <div className="left-box">
        <h2>두근두근</h2>
      </div>
      <div className="right-box">
        <Button style={styles} content="로그인" />
      </div>
    </div>
  );
}

export default LoginPage;
