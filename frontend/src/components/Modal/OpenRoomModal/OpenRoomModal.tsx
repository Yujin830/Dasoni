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
  setMegiAcceptable,
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
      dispatch(setMaster(false));
      dispatch(setRatingLimit(data.ratingLimit));
      dispatch(setMegiAcceptable(data.megiAcceptable));

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
        <div className="box-content">
          <NoLabelInput
            classes="box-title-content"
            type="text"
            value={roomTitle}
            handleChange={handleChangeRoomTitle}
            placeholer="방 제목을 입력하세요."
          />
          <h2>메기 설정</h2>
          <div className="megi">
            <div className="megi-allowed">
              <label
                htmlFor="megiAcceptableCheckbox"
                style={{ display: 'flex', alignItems: 'center' }}
              >
                <div className="allowed" style={{ marginRight: '1vh' }}>
                  메기 입장 가능
                </div>
                {megiAcceptable ? (
                  <svg
                    xmlns="http://www.w3.org/2000/svg"
                    width="34"
                    height="34"
                    viewBox="0 0 34 34"
                    fill="none"
                  >
                    <path
                      d="M17 0C7.616 0 0 7.616 0 17C0 26.384 7.616 34 17 34C26.384 34 34 26.384 34 17C34 7.616 26.384 0 17 0ZM17 30.6C9.486 30.6 3.4 24.514 3.4 17C3.4 9.486 9.486 3.4 17 3.4C24.514 3.4 30.6 9.486 30.6 17C30.6 24.514 24.514 30.6 17 30.6Z"
                      fill="#A45097"
                    />
                    <path
                      d="M17 25.5C21.6944 25.5 25.5 21.6944 25.5 17C25.5 12.3056 21.6944 8.5 17 8.5C12.3056 8.5 8.5 12.3056 8.5 17C8.5 21.6944 12.3056 25.5 17 25.5Z"
                      fill="#A45097"
                    />
                  </svg>
                ) : (
                  <svg
                    xmlns="http://www.w3.org/2000/svg"
                    width="34"
                    height="34"
                    viewBox="0 0 34 34"
                    fill="none"
                  >
                    <path
                      d="M17 0C7.616 0 0 7.616 0 17C0 26.384 7.616 34 17 34C26.384 34 34 26.384 34 17C34 7.616 26.384 0 17 0ZM17 30.6C9.486 30.6 3.4 24.514 3.4 17C3.4 9.486 9.486 3.4 17 3.4C24.514 3.4 30.6 9.486 30.6 17C30.6 24.514 24.514 30.6 17 30.6Z"
                      fill="#A45097"
                    />
                  </svg>
                )}
              </label>
              <input
                type="checkbox"
                id="megiAcceptableCheckbox"
                checked={megiAcceptable}
                onChange={handleChangeMegiAcceptable}
              />
            </div>
            <div className="megi-banned">
              <label
                htmlFor="megiUnacceptableCheckbox"
                style={{ display: 'flex', alignItems: 'center' }}
              >
                <div className="banned" style={{ marginRight: '8px' }}>
                  메기 입장 불가
                </div>
                {!megiAcceptable ? (
                  <svg
                    xmlns="http://www.w3.org/2000/svg"
                    width="34"
                    height="34"
                    viewBox="0 0 34 34"
                    fill="none"
                  >
                    <path
                      d="M17 0C7.616 0 0 7.616 0 17C0 26.384 7.616 34 17 34C26.384 34 34 26.384 34 17C34 7.616 26.384 0 17 0ZM17 30.6C9.486 30.6 3.4 24.514 3.4 17C3.4 9.486 9.486 3.4 17 3.4C24.514 3.4 30.6 9.486 30.6 17C30.6 24.514 24.514 30.6 17 30.6Z"
                      fill="#A45097"
                    />
                    <path
                      d="M17 25.5C21.6944 25.5 25.5 21.6944 25.5 17C25.5 12.3056 21.6944 8.5 17 8.5C12.3056 8.5 8.5 12.3056 8.5 17C8.5 21.6944 12.3056 25.5 17 25.5Z"
                      fill="#A45097"
                    />
                  </svg>
                ) : (
                  <svg
                    xmlns="http://www.w3.org/2000/svg"
                    width="34"
                    height="34"
                    viewBox="0 0 34 34"
                    fill="none"
                  >
                    <path
                      d="M17 0C7.616 0 0 7.616 0 17C0 26.384 7.616 34 17 34C26.384 34 34 26.384 34 17C34 7.616 26.384 0 17 0ZM17 30.6C9.486 30.6 3.4 24.514 3.4 17C3.4 9.486 9.486 3.4 17 3.4C24.514 3.4 30.6 9.486 30.6 17C30.6 24.514 24.514 30.6 17 30.6Z"
                      fill="#A45097"
                    />
                  </svg>
                )}
              </label>
              <input
                type="checkbox"
                id="megiUnacceptableCheckbox"
                checked={!megiAcceptable}
                onChange={() => setOpenMegiAcceptable(!megiAcceptable)}
              />
            </div>
          </div>
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
            {/* <p>{`${convertScoreToName(ratingLimit)}하트 이상만 만나기`}</p> */}
            <p> 하트 이상만 만나기</p>
          </div>
          {/* <div className="modal-background" /> */}
        </div>
        <div className="openroom-button">
          <Button classes="openroom-btn" content="개설하기" handleClick={OpenRoom} />
        </div>
      </div>
    </div>
  );
}

export default OpenRoomModal;
