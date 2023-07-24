import React, { useState } from 'react';
import { useAppSelector } from '../../../app/hooks';
import './MyProfileModify.css';
import BasicInput from '../../Input/BasicInput/BasicInput';

const input = {
  width: '26rem',
  height: '4rem',
  flexShrink: '0',
  borderRadius: '1.25rem',
  border: '3px solid #D9D9D9',
  background: '#FFF',
  color: '#898989',
  fontSize: '1.2rem',
  margin: '0.5rem 0',
  padding: '0.5rem 0.7rem',
};

function MyProfileModify({ setType }: any) {
  const { id, nickname, job, birth } = useAppSelector((state) => state.user);
  console.log(`${id} ${job} ${birth}`);
  const recentUserList = useState([]);
  const faketUserList = [
    { profileImg: 'rank_profile.png', userId: 1 },
    { profileImg: 'rank_profile.png', userId: 2 },
    { profileImg: 'rank_profile.png', userId: 3 },
    { profileImg: 'rank_profile.png', userId: 4 },
    { profileImg: 'rank_profile.png', userId: 5 },
    { profileImg: 'rank_profile.png', userId: 6 },
  ];

  const cancleModify = (e: React.MouseEvent<HTMLAnchorElement>) => {
    e.preventDefault();
    setType('read');
  };

  const [modifyNickname, setModifyNickname] = useState(nickname);
  const [modifySido, setModifySido] = useState('');
  const [modifyGugun, setModifyGugun] = useState('');
  const [modifyJob, setModifyJob] = useState(job);

  const handleBirthChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setModifyNickname(e.target.value);
  };
  // const handleSidoChange = (e: React.ChangeEvent<HTMLInputElement>) => {
  //   setModifyBirth(e.target.value);
  // };
  // const handleGugunChange = (e: React.ChangeEvent<HTMLInputElement>) => {
  //   setModifyBirth(e.target.value);
  // };
  const handleJobChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setModifyJob(e.target.value);
  };

  const modifyUserProfile = (e: React.MouseEvent<HTMLAnchorElement>) => {
    e.preventDefault();
    console.log('수정 완료');
  };

  return (
    <div className="content">
      <p id="user">
        <span id="name">
          {nickname}({id})
        </span>
        님의 개인정보
      </p>
      <div className="modify-form">
        <BasicInput
          style={input}
          label="닉네임"
          type="text"
          value={String(modifyNickname)}
          handleChange={handleBirthChange}
        />
        <BasicInput
          style={input}
          label="주소"
          type="text"
          value={String(modifySido)}
          handleChange={handleBirthChange}
        />
        <BasicInput
          style={input}
          label="직업"
          type="text"
          value={String(modifyJob)}
          handleChange={handleJobChange}
        />
      </div>
      <footer>
        <a className="btn modify" href="/" onClick={modifyUserProfile}>
          정보 수정 완료
        </a>
        <a className="btn remove" href="/" onClick={cancleModify}>
          취소
        </a>
      </footer>
    </div>
  );
}

export default MyProfileModify;
