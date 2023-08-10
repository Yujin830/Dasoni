import React, { useState, useEffect } from 'react';
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
  const [nickname, setNickname] = useState('');
  const [job, setJob] = useState('');
  const [modifySido, setModifySido] = useState(11); // "서울특별시"의 sidoCode인 "11"을 설정
  const [modifyGugun, setModifyGugun] = useState(0); // 초기값은 빈 문자열
  const { memberId, profileImageSrc } = useAppSelector((state) => state.user);
  const navigate = useNavigate();

  const handleChangeJob = (event: React.ChangeEvent<HTMLInputElement>) =>
    setJob(event.target.value);

  const handleChangeNickname = (event: React.ChangeEvent<HTMLInputElement>) =>
    setNickname(event.target.value);
  const handleSkip = () => {
    onClose(); // 모달을 닫습니다.
    navigate('/main'); // 건너뛰기 버튼을 누르면 MainPage로 이동합니다.
  };

  const dispatch = useAppDispatch();

  const AddProfile = async (event: React.MouseEvent<HTMLButtonElement>) => {
    event.preventDefault();

    const data = {
      memberId: memberId,
      sido: modifySido,
      gugun: modifyGugun,
      job: job,
      nickname: nickname,
      profileImageSrc: profileImageSrc,
    };
    try {
      await dispatch(modifyUserAsync(data));
      console.log('프로필 정보 추가');
      console.log(data);
      onClose();
      navigate('/main');
    } catch (error) {
      console.log('프로필 정보 추가 실패:', error);
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
            {/* <label htmlFor="label nickname">닉네임</label> */}
            <Input
              label="닉네임"
              labelClass="profile-label"
              classes="profile-input"
              type="text"
              value={nickname}
              placeholer="닉네임을 입력해주세요"
              handleChange={handleChangeNickname}
            />
          </div>
          <div className="input job">
            {/* <label htmlFor="label job">직업</label> */}
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
