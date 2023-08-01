import React, { useCallback, useEffect, useRef, useState } from 'react';
import Header from '../../components/Header/Header';
import axios from 'axios';
import './MainPage.css';
import Banner from '../../components/Banner/Banner';
import IconButton from '../../components/Button/IconButton';
import NoLableInput from '../../components/Input/NoLabelInput/NoLabelInput';
import RoomBox from '../../components/RoomBox/RoomBox';
import FilledButton from '../../components/Button/FilledButton';
import { useNavigate } from 'react-router';
import { useDispatch, useSelector } from 'react-redux';
import { RootState } from '../../app/store';
import {
  setMaster,
  setMegiAcceptable,
  setRatingLimit,
  setRoomTitle,
  setWaitingRoomId,
  setHelpModalVisible,
  setOpenRoomModalVisible,
} from '../../app/slices/waitingSlice';
import { WaitingRoomInfoRes } from '../../apis/response/waitingRoomRes';

import HelpModal from '../../components/Modal/HelpModal/HelpModal';
import OpenRoomModal from '../../components/Modal/OpenRoomModal/OpenRoomModal';

const styles = {
  iconBtn: {
    width: '5rem',
    height: '2.5rem',
    borderRadius: '6.25rem',
    background: 'rgba(238, 114, 165, 0.50)',
  },
  searchBar: {
    width: '16rem',
    height: '2.5rem',
    borderRadius: '1.5rem',
    background: '#FFE8EF',
    color: '#555',
    fontSize: '0.8rem',
    border: 'none',
    paddingLeft: '1rem',
  },
  createRoomBtn: {
    width: '8rem',
    height: '3rem',
    borderRadius: '0.5rem',
    background: '#ECC835',
    color: '#fff',
    fontSize: '1.2rem',
    fontWeight: '600',
  },
  fastMatchBtn: {
    width: '8rem',
    height: '3rem',
    borderRadius: '0.5rem',
    background: '#EC5E98',
    color: '#fff',
    fontSize: '1.2rem',
    fontWeight: '600',
  },
  pagenationBtn: {
    width: '5.5rem',
    height: '2.5rem',
    borderRadius: '6.25rem',
    background: '#FFE8EF',
    color: '#555',
    fontSize: '1rem',
  },
};

function MainPage() {
  //모달 띄우기
  const helpModalVisible = useSelector((state: RootState) => state.waitingRoom.helpModalVisible);
  const openRoomModalVisible = useSelector(
    (state: RootState) => state.waitingRoom.openRoomModalVisible,
  );

  // Toggle HelpModal visibility
  const handleHelpModalToggle = () => {
    dispatch(setHelpModalVisible(!helpModalVisible));
  };
  // Toggle OpenRoomModal visibility
  const handleOpenRoomModalToggle = () => {
    dispatch(setOpenRoomModalVisible(!openRoomModalVisible));
  };

  // 필터 버튼 토클
  const [isOpen, setIsOpen] = useState(false);
  const handleToggleFilter = () => {
    setIsOpen((prevState) => !prevState);
  };

  // 검색창 입력값
  const [searchInput, setSearchInput] = useState('');
  const handleSearchInput = (e: React.ChangeEvent<HTMLInputElement>) => {
    setSearchInput(e.target.value);
  };

  // 검색
  const search = (searchInput: string) => {
    if (searchInput === '') {
      alert('검색어를 입력해주세요');
      return;
    }

    console.log(searchInput);
    // TODO : 검색 API 로직 개발
  };

  // 미팅 대기방 리스트
  const [waitingRoomList, setWaitingRoomList] = useState<WaitingRoomInfoRes[]>([]);
  const getWaitingRoomList = async () => {
    try {
      const res = await axios.get('/rooms');
      console.log(res);
      if (res.status === 200) {
        setWaitingRoomList(res.data.content.content);
      }
    } catch (err) {
      console.error(err);
    }
  };
  useEffect(() => {
    getWaitingRoomList();
  }, []);

  // 방 만들기 모달 open
  const navigate = useNavigate();
  const dispatch = useDispatch();
  const createRoom = () => {
    // TODO : 클릭 시 모달 띄우기
    // setOpenRoomModalVisible(true);
    console.log('방 만들기');

    // const fakeData = {
    //   memberId: 1,
    //   title: '함께 놀아요',
    //   ratingLimit: 300,
    //   megiAcceptable: false,
    // };
    // const res = await axios.post('/rooms', fakeData);
    // console.log(res);
    // if (res.data.status.code === 5000) {
    //   // 리덕스에 생성한 대기방 정보 저장
    //   dispatch(setWaitingRoomId(res.data.content.createdRoomId));
    //   dispatch(setRoomTitle(fakeData.title));
    //   dispatch(setMaster(false));
    //   dispatch(setRatingLimit(fakeData.ratingLimit));
    //   dispatch(setMegiAcceptable(fakeData.megiAcceptable));

    //   // TODO : 모달 닫기
    //   setOpenRoomModalVisible(false); // Set modalVisible to false to close the modal
    //   // 대기방으로 이동
    //   navigate('/waiting-room');
  };

  // 빠른 매칭 모달 open
  const matchFast = () => {
    alert('빠른 매칭 진행 중');
  };

  // 새로고침 버튼 클릭
  const onClickRefreshBtn = () => {
    console.log('새로고침');
    getWaitingRoomList();
  };

  return (
    <div id="main" className={openRoomModalVisible ? 'modal-visible' : ''}>
      <Header
        onModalToggle={handleHelpModalToggle}
        // onOpenRoomModalToggle={handleOpenRoomModalToggle}
      />
      <Banner />
      <main>
        <div id="main-top">
          <button className="refresh" onClick={onClickRefreshBtn}>
            <span className="material-symbols-outlined">refresh</span>
          </button>
          <div id="filter-box">
            <IconButton
              style={styles.iconBtn}
              content="필터"
              iconPosition="left"
              icon="filter_list"
              handleClick={handleToggleFilter}
            />
            <ul className={isOpen ? 'show' : ''}>
              <li>남자만 입장 가능</li>
              <li>여자만 입장 가능</li>
            </ul>
          </div>
          <div id="search-box">
            <NoLableInput
              style={styles.searchBar}
              type="text"
              value={searchInput}
              placeholer="검색어를 입력해주세요."
              handleChange={handleSearchInput}
            />
            <button
              className="material-symbols-outlined search-icon"
              onClick={() => search(searchInput)}
            >
              search
            </button>
          </div>
        </div>
        <div className="room-container">
          {waitingRoomList.length > 0
            ? waitingRoomList.map((room) => (
                <RoomBox
                  key={room.roomId}
                  roomId={room.roomId}
                  title={room.title}
                  malePartyMemberCount={room.malePartyMemberCount}
                  femalePartyMemberCount={room.femalePartyMemberCount}
                  malePartyAvgRating={room.malePartyAvgRating}
                  femalePartyAvgRating={room.femalePartyAvgRating}
                  megiAcceptable={room.megiAcceptable}
                />
              ))
            : //       src="resources/images/openvidu_grey_bg_transp_cropped.png"
              //       alt="OpenVidu logo"
              //     />
              //   </div>
              //   <div id="join-dialog" className="jumbotron vertical-center">
              //     <h1> Join a video session </h1>
              //     <form className="form-group" onSubmit={joinSession}>
              //       <p>
              //         <label htmlFor="userName">Participant: </label>
              //         <input
              //           className="form-control"
              //           type="text"
              //           id="userName"
              //           value={myUserName}
              //           onChange={handleChangeUserName}
              //           required
              //         />
              //       </p>
              //       <p>
              //         <label htmlFor="sessionId"> Session: </label>
              //         <input
              //           className="form-control"
              //           type="text"
              //           id="sessionId"
              //           value={mySessionId}
              //           onChange={handleChangeSessionId}
              //           required
              //         />
              //       </p>
              //       <p className="text-center">
              //         <input
              //           className="btn btn-lg btn-success"
              //           name="commit"
              //           type="submit"
              //           value="JOIN"
              //         />
              //       </p>
              //     </form>
              //   </div>
              // </div>
              null}

          {/* {session !== undefined ? (
            <div id="session">
              <div id="session-header">
                <h1 id="session-title">{mySessionId}</h1>
                <input
                  className="btn btn-large btn-danger"
                  type="button"
                  id="buttonLeaveSession"
                  onClick={leaveSession}
                  value="Leave session"
                />
                <input
                  className="btn btn-large btn-success"
                  type="button"
                  id="buttonSwitchCamera"
                  onClick={switchCamera}
                  value="Switch Camera"
                />
              </div>

              {mainStreamManager !== undefined ? (
                <div id="main-video" className="col-md-6">
                  <UserVideo streamManager={mainStreamManager} />
                </div>
              ) : null}
              <div id="video-container" className="col-md-6">
                {publisher !== undefined ? (
                  <a
                    href="/"
                    className="stream-container col-md-6 col-xs-6"
                    onClick={(e) => handleMainVideoStream(publisher, e)}
                  >
                    <UserVideo streamManager={publisher} />
                  </a>
                ) : null}
                {subscribers.map((sub, i) => (
                  <a
                    href="/"
                    key={sub.id}
                    className="stream-container col-md-6 col-xs-6"
                    onClick={(e) => handleMainVideoStream(sub, e)}
                  >
                    <span>{sub.id}</span>
                    <UserVideo streamManager={sub} />
                  </a>
                ))}
              </div>
            </div>
          ) : null} */}
        </div>
        <div id="room-footer">
          <div id="btn-box">
            <FilledButton
              content="방 만들기"
              style={styles.createRoomBtn}
              handleClick={handleOpenRoomModalToggle}
            />
            <FilledButton content="빠른 매칭" style={styles.fastMatchBtn} handleClick={matchFast} />
          </div>
          <div id="pagenationBtn-box">
            <IconButton
              style={styles.pagenationBtn}
              content="이전"
              iconPosition="left"
              handleClick={createRoom}
              icon="chevron_left"
            />
            <IconButton
              style={styles.pagenationBtn}
              content="다음"
              iconPosition="right"
              handleClick={createRoom}
              icon="chevron_right"
            />
          </div>
        </div>
      </main>
      {/* HelpModal 컴포넌트를 렌더링합니다. */}
      {helpModalVisible && <HelpModal onClose={handleHelpModalToggle} />}

      {/* OpenRoomModal 컴포넌트를 렌더링합니다. */}
      {openRoomModalVisible && <OpenRoomModal onClose={handleOpenRoomModalToggle} />}
    </div>
  );
}

export default MainPage;
