import React, { useCallback, useEffect, useRef, useState } from 'react';
import Header from '../../components/Header/Header';
import axios from 'axios';
import './MainPage.css';
import Banner from '../../components/Banner/Banner';
import IconButton from '../../components/Button/IconButton';
import NoLableInput from '../../components/Input/NoLabelInput/NoLabelInput';
import RoomBox, { RoomBoxProps } from '../../components/RoomBox/RoomBox';
import FilledButton from '../../components/Button/FilledButton';
import { useNavigate } from 'react-router';
import { useDispatch, useSelector } from 'react-redux';
import { RootState } from '../../app/store';
import {
  setHelpModalVisible,
  setOpenQuickMatchingModalVisible,
  setOpenRoomModalVisible,
} from '../../app/slices/waitingSlice';
import { WaitingRoomInfoRes } from '../../apis/response/waitingRoomRes';
import { useAppSelector } from '../../app/hooks';
import MatchingModal from '../../components/Modal/MatchingModal/MatchingModal';
import HelpModal from '../../components/Modal/HelpModal/HelpModal';
import OpenRoomModal from '../../components/Modal/OpenRoomModal/OpenRoomModal';
import SkeletonElement from '../../components/SkeletonElement/SkeletonElement';
import SkeletonMainPage from './SkeletonMainPage';

// 서버 주소를 환경에 따라 설정
const APPLICATION_SERVER_URL =
  process.env.NODE_ENV === 'production' ? '' : 'https://demos.openvidu.io/';

function MainPage() {
  // 미팅 대기방 리스트

  //모달 띄우기
  const helpModalVisible = useSelector((state: RootState) => state.waitingRoom.helpModalVisible);
  const openRoomModalVisible = useSelector(
    (state: RootState) => state.waitingRoom.openRoomModalVisible,
  );
  // const openQuickMatchingModalVisible = useSelector(
  //   (state: RootState) => state.waitingRoom.openQuickMatchingModalVisible,
  // );

  // Toggle HelpModal visibility
  const handleHelpModalToggle = () => {
    dispatch(setHelpModalVisible(!helpModalVisible));
  };
  // Toggle OpenRoomModal visibility
  const handleOpenRoomModalToggle = () => {
    dispatch(setOpenRoomModalVisible(!openRoomModalVisible));
  };

  // const handleOpenQuickMatchingToggle = () => {
  //   dispatch(setOpenQuickMatchingModalVisible(!openQuickMatchingModalVisible));
  // };

  const [isFastModalOpen, setFastModalOpen] = useState(false);
  const [isMegiModalOpen, setMegiModalOpen] = useState(false);

  // 필터 버튼 토클
  const [isOpen, setIsOpen] = useState(false);
  const handleToggleFilter = () => {
    setIsOpen((prevState) => !prevState);
  };

  // 필터 적용
  const handleClickFilter = async (gender: string) => {
    console.log(gender);

    try {
      const res = await axios.get(`/api/rooms/filter/${gender}`);
      console.log(res);

      if (res.status === 200) {
        setWaitingRoomList(res.data.content.content);
      }
    } catch (err) {
      console.error(err);
    }
  };

  // 검색창 입력값
  const [searchInput, setSearchInput] = useState('');
  const handleSearchInput = (e: React.ChangeEvent<HTMLInputElement>) => {
    setSearchInput(e.target.value);
  };

  // 검색
  const search = async (searchInput: string) => {
    if (searchInput === '') {
      getWaitingRoomList();
      return;
    }

    try {
      const res = await axios.get(`/api/rooms/search/${searchInput}`);

      if (res.status === 200) {
        setWaitingRoomList(res.data.content.content);
      }
    } catch (err) {
      console.error(err);
    }
  };

  // 미팅 대기방 리스트
  const [waitingRoomList, setWaitingRoomList] = useState<WaitingRoomInfoRes[]>([]);
  const [currentPage, setCurrentPage] = useState(0); // 현재 페이지 상태
  const getWaitingRoomList = async () => {
    try {
      const res = await axios.get('/api/rooms');
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

  const navigate = useNavigate();
  const dispatch = useDispatch();
  // const createRoom = () => {
  //   console.log('방 만들기');
  // };

  // 방목록 페이지 넘기기
  const nextRoomList = async () => {
    try {
      const nextPage = currentPage + 1; // 현재 페이지에서 1 증가
      const res = await axios.get(`/api/rooms?page=${nextPage}`);
      console.log('다음');
      if (res.status === 200) {
        const nextPageData = res.data.content.content;

        if (nextPageData.length > 0) {
          setCurrentPage(nextPage);
          setWaitingRoomList(nextPageData);
        } else {
          console.log('다음 페이지에 방이 없습니다.');
          // 다음 페이지에 방이 없을 경우 처리
        }
      }
    } catch (err) {
      console.error(err);
    }
  };

  const prevRoomList = async () => {
    try {
      const prevPage = Math.max(currentPage - 1, 0); // 현재 페이지에서 1 감소, 최소 0 이상
      const res = await axios.get(`/api/rooms?page=${prevPage}`);
      console.log('이전');
      if (res.status === 200) {
        setCurrentPage(prevPage);
        console.log('현재페이지:', prevPage, 'content:', res.data.content);
        setWaitingRoomList(res.data.content.content);
      }
    } catch (err) {
      console.error(err);
    }
  };

  const member = useAppSelector((state) => state.user);

  // 빠른 매칭 모달 open
  const matchFast = async () => {
    const memberId = member.memberId;

    try {
      const res = await axios.post(`api/match/members/${memberId}`);

      if (res.status === 200) {
        console.log('빠른 매치 응답 : ', res.data);
        setFastModalOpen(true);
      } else {
        alert('빠른 매칭 중 오류가 발생했습니다.');
      }
    } catch (error) {
      console.log('빠른 매칭 오류', error);
      alert('빠른 매칭 중 오류가 발생했습니다.');
    }
  };

  //메기 매칭
  const megiMatch = async () => {
    const memberId = member.memberId;

    try {
      const res = await axios.post(`api/match/members/${memberId}/special`);

      if (res.status === 200) {
        console.log('메기 매치 응답 : ', res.data);
        setMegiModalOpen(true);
      } else {
        alert('메기 매칭 중 오류가 발생했습니다.');
      }
    } catch (error) {
      console.log('메기 매칭 오류', error);
      alert('메기 매칭 중 오류가 발생했습니다.');
    }
  };

  // 새로고침 버튼 클릭
  const onClickRefreshBtn = () => {
    console.log('새로고침');
    getWaitingRoomList();
  };

  return (
    <div id="main" className={openRoomModalVisible ? 'modal-visible' : ''}>
      <Header onModalToggle={handleHelpModalToggle} />
      <Banner />
      <main>
        <div id="main-top">
          <button className="refresh" onClick={onClickRefreshBtn}>
            <span className="material-symbols-outlined">refresh</span>
          </button>
          <div id="filter-box">
            <IconButton
              classes="filter-btn"
              content="필터"
              iconPosition="left"
              icon="filter_list"
              handleClick={handleToggleFilter}
            />
            <ul className={isOpen ? 'show' : ''}>
              <li role="presentation" onClick={() => handleClickFilter('male')}>
                남자만 입장 가능
              </li>
              <li role="presentation" onClick={() => handleClickFilter('female')}>
                여자만 입장 가능
              </li>
            </ul>
          </div>
          <div id="search-box">
            <NoLableInput
              classes="search-bar"
              type="text"
              value={searchInput}
              placeholer="검색하기"
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
                  maleMemberCount={room.maleMemberCount}
                  femaleMemberCount={room.femaleMemberCount}
                  maleAvgRating={room.maleAvgRating}
                  femaleAvgRating={room.femaleAvgRating}
                  megiAcceptable={room.megiAcceptable}
                  ratingLimit={room.ratingLimit}
                />
              ))
            : '존재하는 방이 없습니다.'}
          {!waitingRoomList && <SkeletonMainPage />}
        </div>
        <div id="room-footer">
          <div id="pagenationBtn-box">
            <IconButton
              classes="page-btn"
              content="이전"
              iconPosition="left"
              handleClick={prevRoomList}
              icon="chevron_left"
            />
            <IconButton
              classes="page-btn"
              content="다음"
              iconPosition="right"
              handleClick={nextRoomList}
              icon="chevron_right"
            />
          </div>
          <div id="btn-box">
            <FilledButton
              classes="create-room-btn"
              content="방 만들기"
              handleClick={handleOpenRoomModalToggle}
            />
            <FilledButton classes="fast-match-btn" content="빠른 매칭" handleClick={matchFast} />
            <FilledButton classes="megi-match-btn" content="메기 입장" handleClick={megiMatch} />
          </div>
        </div>
      </main>
      {/* HelpModal 컴포넌트를 렌더링합니다. */}
      {helpModalVisible && <HelpModal onClose={handleHelpModalToggle} />}
      <div className={`helpmodal-overlay ${helpModalVisible == true ? 'active' : ''}`}></div>
      {/* OpenRoomModal 컴포넌트를 렌더링합니다. */}
      {openRoomModalVisible && <OpenRoomModal onClose={handleOpenRoomModalToggle} />}
      <div
        className={`openroommodal-overlay ${openRoomModalVisible == true ? 'active' : ''}`}
      ></div>
      {/* MatchingModal 컴포넌트를 렌더링합니다. */}
      {/* {openQuickMatchingModalVisible && <MatchingModal onClose={handleOpenQuickMatchingToggle} />} */}
      {isFastModalOpen && <MatchingModal onClose={() => setFastModalOpen(false)} />}
      {isMegiModalOpen && <MatchingModal onClose={() => setMegiModalOpen(false)} />}
      {/* <div
        className={`matchingmodal-overlay ${openQuickMatchingModalVisible == true ? 'active' : ''}`}
      ></div> */}
    </div>
  );
}

export default MainPage;
