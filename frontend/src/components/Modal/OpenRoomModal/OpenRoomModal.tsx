import React, { useRef, useState } from 'react';
import Select from 'react-select';
// import '../Modal.css';
import './OpenRoomModal.css';
import NoLabelInput from '../../Input/NoLabelInput/NoLabelInput';
import Button from '../../Button/FilledButton';
import { useDispatch, useSelector } from 'react-redux';
import { useNavigate, useParams } from 'react-router';
import axios from 'axios';
import {
  setMaster,
  setRatingLimit,
  setRoomTitle,
  setWaitingRoomId,
  setWaitingMemberList,
  setRoomType,
} from '../../../app/slices/waitingSlice';
import convertScoreToName from '../../../utils/convertScoreToName';
import { useAppSelector } from '../../../app/hooks';

interface OpenRoomModalProps {
  onClose: () => void;
}

function OpenRoomModal({ onClose }: OpenRoomModalProps) {
  const [roomTitle, setOpenRoomTitle] = useState('');
  const [megiAcceptable, setOpenMegiAcceptable] = useState(false);
  const [ratingLimit, setOpenRatingLimit] = useState<number>(0);
  const { roomId } = useParams();
  // const { memberId } = useAppSelector((state) => state.user);
  const member = useAppSelector((state) => state.user);

  const handleChangeRoomTitle = (event: React.ChangeEvent<HTMLInputElement>) =>
    setOpenRoomTitle(event.target.value);

  const handleChangeMegiAcceptable = (event: React.ChangeEvent<HTMLInputElement>) =>
    setOpenMegiAcceptable(event.target.checked);

  const handleChangeRatingLimit = (event: React.ChangeEvent<HTMLSelectElement>) =>
    setOpenRatingLimit(Number(event.target.value));

  const dispatch = useDispatch();
  const navigate = useNavigate();

  const OpenRoom = async (event: React.MouseEvent<HTMLButtonElement>) => {
    event.preventDefault();

    // 남은 라이프 확인
    if (member.remainLife === 0) {
      alert('오늘은 모든 라이프를 소진하여 더 이상 입장할 수 없습니다.');
      return;
    }

    const data = {
      memberId: member.memberId,
      title: roomTitle,
      megiAcceptable: megiAcceptable,
      ratingLimit: ratingLimit,
    };
    // 백엔드로 data 디스패치 하는 API 호출

    console.log(data);
    const res = await axios.post('/api/rooms', data);
    console.log(res);
    if (res.status === 200) {
      // 리덕스에 생성한 대기방 정보 저장
      dispatch(setWaitingRoomId(res.data.content.createdRoomId));
      dispatch(setRoomType('private'));
      dispatch(setRoomTitle(data.title));
      dispatch(setRatingLimit(data.ratingLimit));

      const waitingMember = {
        member: {
          memberId: member.memberId,
          nickname: member.nickname,
          gender: member.gender,
          profileImageSrc: member.profileImageSrc,
          rating: member.rating,
          meetingCount: member.matchCnt,
          job: member.job,
        },
        roomLeader: true,
        specialUser: false,
      };
      dispatch(setWaitingMemberList([waitingMember]));

      // TODO : 모달 닫기
      onClose();
      // 대기방으로 이동
      navigate(`/waiting-room/${res.data.content.createdRoomId}`);
    }
  };
  return (
    <div className="openroom-modal">
      <div className="header">
        방만들기
        <div className="close-button">
          <button onClick={onClose}>X</button>
        </div>
      </div>
      <div className="box">
        <div className="room_title">
          <h2>방 제목 설정</h2>
          <NoLabelInput
            classes="box-title-content"
            type="text"
            value={roomTitle}
            handleChange={handleChangeRoomTitle}
            placeholer="방 제목을 입력하세요."
          />
        </div>
        <div className="rank_limit">
          <h2>랭크 제한 설정</h2>
          <div className="rank-content">
            <select className="select-rank" value={ratingLimit} onChange={handleChangeRatingLimit}>
              <option value="">랭크를 선택하세요.</option>
              <option value="0">하얀</option>
              <option value="300">노랑</option>
              <option value="500">초록</option>
              <option value="1000">보라</option>
              <option value="1500">파랑</option>
              <option value="2000">빨강</option>
              <option value="2500">무지개</option>
            </select>
            <p> 하트 이상만 만나기</p>
          </div>
        </div>
        <div className="openroom-button">
          <Button classes="openroom-btn" content="개설하기" handleClick={OpenRoom} />
        </div>
      </div>
    </div>
  );
}

export default OpenRoomModal;
