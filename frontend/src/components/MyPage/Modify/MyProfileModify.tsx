import React, { useState } from 'react';
import { useAppSelector, useAppDispatch } from '../../../app/hooks';
import './MyProfileModify.css';
import BasicInput from '../../Input/BasicInput/BasicInput';
import AddressSelecter from '../../Element/AddressSelecter/AddressSelecter';

import { modifyUserAsync } from '../../../app/slices/user';

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
  const { id, nickname, job, memberId } = useAppSelector((state) => state.user);

  const cancleModify = (e: React.MouseEvent<HTMLAnchorElement>) => {
    e.preventDefault();
    setType('read');
  };

  const [modifyNickname, setModifyNickname] = useState(nickname);
  const [modifySido, setModifySido] = useState('11');
  const [modifyGugun, setModifyGugun] = useState('');
  const [modifyJob, setModifyJob] = useState(job);
  const [modifyProfileSrc, setModifyProfileSrc] = useState('');

  const handleBirthChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setModifyNickname(e.target.value);
  };
  const handleJobChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setModifyJob(e.target.value);
  };
  const handleProfileSrcChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setModifyProfileSrc(e.target.value);
  };

  const dispatch = useAppDispatch();
  const modifyUserProfile = async (e: React.MouseEvent<HTMLAnchorElement>) => {
    e.preventDefault();
    const modifiedData = {
      memberId: memberId,
      sido: Number(modifySido),
      gugun: Number(modifyGugun),
      job: modifyJob,
      nickname: modifyNickname,
      profileImageSrc: modifyProfileSrc,
    };
    console.log(modifiedData);

    dispatch(modifyUserAsync(modifiedData));
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
          label="프로필"
          type="file"
          value={String(modifyProfileSrc)}
          handleChange={handleProfileSrcChange}
        />
        <BasicInput
          style={input}
          label="닉네임"
          type="text"
          value={String(modifyNickname)}
          handleChange={handleBirthChange}
        />
        <AddressSelecter
          modifySido={modifySido}
          modifyGugun={modifyGugun}
          setModifySido={setModifySido}
          setModifyGugun={setModifyGugun}
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
