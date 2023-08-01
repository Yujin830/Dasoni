import React, { useState } from 'react';
import '../Modal.css';
import './ProfileModal.css';
import Input from '../../Input/BasicInput/BasicInput';
import Button from '../../Button/FilledButton';
import { useAppDispatch } from '../../../app/hooks';
import { modifyUserAsync } from '../../../app/slices/user';
const styles = {
  button: {
    width: '10rem',
    height: '4rem',
    flexShrink: '0',
    borderRadius: '1.25rem',
    background: '#EC5E98',
    color: '#FFF',
    fontSize: '1.75rem',
    fontStyle: 'normal',
    fontWeight: '700',
  },
  input: {
    width: '26rem',
    height: '3rem',
    flexShrink: '0',
    borderRadius: '0.8rem',
    border: '3px solid #D9D9D9',
    background: '#FFF',
    color: '#898989',
    fontSize: '1rem',
    margin: '0.5rem 0',
    padding: '0.5rem 0.7rem',
  },
};

function ProfileModal() {
  const [height, setHeight] = useState('');
  const [job, setJob] = useState('');
  const [hobby, setHobby] = useState('');
  const [address, setAddress] = useState('');
  const [nickname, setNickname] = useState('');

  const handleChangeHeight = (event: React.ChangeEvent<HTMLInputElement>) =>
    setHeight(event.target.value);
  const handleChangeJob = (event: React.ChangeEvent<HTMLInputElement>) =>
    setJob(event.target.value);
  const handleChangeHobby = (event: React.ChangeEvent<HTMLInputElement>) =>
    setHobby(event.target.value);
  const handleChangeAddress = (event: React.ChangeEvent<HTMLInputElement>) =>
    setAddress(event.target.value);
  const handleChangeNickname = (event: React.ChangeEvent<HTMLInputElement>) =>
    setNickname(event.target.value);

  const dispatch = useAppDispatch();

  const AddProfile = (event: React.MouseEvent<HTMLButtonElement>) => {
    event.preventDefault();

    const data = {
      height: height,
      job: job,
      hobby: hobby,
      address: address,
      nickname: nickname,
    };

    dispatch(modifyUserAsync(data));
    console.log('addprofile');
    console.log(data);
  };

  return (
    <div className="modal">
      <div className="header">
        프로필 정보 추가
        <button className="pass-button">건너뛰기</button>
      </div>
      <div className="box">
        <h3>이성에게 어필할 수 있는 나의 정보를 더 작성해주세요!</h3>
        <div className="inputbox">
          <div className="input height">
            <label htmlFor="label height">키</label>
            <Input
              style={styles.input}
              type="text"
              value={height}
              handleChange={handleChangeHeight}
            />
          </div>
          <div className="input job">
            <label htmlFor="label job">직업</label>
            <Input style={styles.input} type="text" value={job} handleChange={handleChangeJob} />
          </div>
          <div className="input hoby">
            <label htmlFor="label hoby">취미</label>
            <Input
              style={styles.input}
              type="text"
              value={hobby}
              handleChange={handleChangeHobby}
            />
          </div>
          <div className="input address">
            <label htmlFor="label address">사는곳</label>
            <Input
              style={styles.input}
              type="text"
              value={address}
              handleChange={handleChangeAddress}
            />
          </div>
          <div className="input nickname">
            <label htmlFor="label nickname">닉네임</label>
            <Input
              style={styles.input}
              type="text"
              value={nickname}
              handleChange={handleChangeNickname}
            />
          </div>
        </div>
        <div className="complete-button">
          <Button style={styles.button} content="완료" handleClick={AddProfile} />
        </div>
      </div>
    </div>
  );
}

export default ProfileModal;
