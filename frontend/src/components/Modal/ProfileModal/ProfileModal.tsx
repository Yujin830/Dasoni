import React, { useState } from 'react';
import '../Modal.css';
import './ProfileModal.css';
import Input from '../../Input/BasicInput/BasicInput';
import Button from '../../Button/FilledButton';
import { useAppDispatch, useAppSelector } from '../../../app/hooks';
import { modifyUserAsync } from '../../../app/slices/user';
import { useNavigate } from 'react-router-dom';
import AddressSelecter from '../../Element/AddressSelecter/AddressSelecter';

interface ProfileModalProps {
  onClose: () => void;
}

function ProfileModal({ onClose }: ProfileModalProps) {
  const { memberId, profileImageSrc } = useAppSelector((state) => state.user);
  const [nickname, setNickname] = useState<string>('');
  const [job, setJob] = useState<string>('');
  const [modifySido, setModifySido] = useState<number>(11);
  const [modifyGugun, setModifyGugun] = useState<number>(0);
  const [nickNameError, setNickNameError] = useState<string | null>(null);
  const dispatch = useAppDispatch(); // Ensure you have this dispatch defined.
  const navigate = useNavigate();

  const handleChangeJob = (event: React.ChangeEvent<HTMLInputElement>) =>
    setJob(event.target.value);

  const handleChangeNickname = (event: React.ChangeEvent<HTMLInputElement>) => {
    const value = event.target.value;
    setNickname(value);

    if (value.length > 10) {
      setNickNameError('닉네임은 10글자 이내로 입력해주세요');
    } else {
      setNickNameError(null);
    }
  };

  const handleSkip = () => {
    onClose();
    navigate('/main');
  };

  const AddProfile = async (event: React.MouseEvent<HTMLButtonElement>) => {
    event.preventDefault();

    if (!nickname || nickname.length > 10 || !job) {
      window.alert('모든 내용를 올바르게 입력해주세요.');
      return;
    }

    const data = {
      memberId: memberId,
      sido: modifySido,
      gugun: modifyGugun,
      job: job,
      nickname: nickname,
      profileImageSrc: 'null',
    };

    try {
      await dispatch(modifyUserAsync(data));
      onClose();
      navigate('/main');
    } catch (error) {
      console.error('프로필 정보 추가 실패:', error);
    }
  };

  return (
    <div className="profile_modal">
      <div className="header">
        프로필 정보 추가
        <button className="pass-button" onClick={handleSkip}>
          건너뛰기
        </button>
      </div>
      <div className="box">
        <h3>
          이성에게 어필할 수 있는 <br className="mobile" /> 나의 정보를 더 작성해주세요!
        </h3>
        <div className="inputbox">
          <div className="input nickname">
            <Input
              label="닉네임"
              labelClass="profile-label"
              classes="profile-input"
              type="text"
              value={nickname}
              placeholer="닉네임을 입력해주세요 (10글자 이내)"
              handleChange={handleChangeNickname}
            />
          </div>
          {nickNameError && <div className="nickname-error-message">{nickNameError}</div>}
          <div className="input job">
            <Input
              label="직업"
              labelClass="profile-label"
              classes="profile-input"
              type="text"
              value={job}
              handleChange={handleChangeJob}
              placeholer="직업을 입력해주세요"
            />
          </div>
          <div className="input address">
            <AddressSelecter
              modifySido={modifySido}
              modifyGugun={modifyGugun}
              setModifySido={setModifySido}
              setModifyGugun={setModifyGugun}
            />
          </div>
        </div>
        <div className="complete-button">
          <Button classes="profile-complete-btn" content="완료" handleClick={AddProfile} />
        </div>
      </div>
    </div>
  );
}

export default ProfileModal;
