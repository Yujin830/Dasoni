import React, { useEffect, useState } from 'react';
import './RecentMatchAvartar.css';
import axios from 'axios';
import { useAppSelector } from '../../../app/hooks';
import MatchChatRoom from '../../ChatRoom/MatchChatRoom/MatchChatRoom';

type recentMatchAvartar = {
  src: string;
  gender: string;
  matchedMemberIndex: number;
  matchedMemberId: number;
  recentUserList: any[];
  setRecentUserList: (newMemberList: any[]) => void;
};

function RecentMatchAvartar({
  src,
  gender,
  matchedMemberIndex,
  matchedMemberId,
  recentUserList,
  setRecentUserList,
}: recentMatchAvartar) {
  const [isHover, setIsHover] = useState(false); // 프로필이미지에 마우스 올림 / 내림
  const [sendBtn, setSendBtn] = useState(false); // 채팅 메세지 send / close
  const { memberId } = useAppSelector((state) => state.user);

  // 마우스 올렸을 때, 삭제 버튼 보이기
  const handleMouseOver = () => {
    setIsHover(true);
  };
  const handleFocus = () => {
    console.log('focus on');
  };

  // 마우스 밖에 나왔을 때, 삭제 버튼 사라지게
  const handleMouseOut = () => {
    setIsHover(false);
  };
  const handleBlur = () => {
    console.log('focus out');
  };

  // 클릭 시 삭제
  const handleDeleteMatchMember = async () => {
    console.log('매칭 다소니 삭제');
    const res = await axios.delete(`/api/users/${memberId}/history`, {
      data: {
        targetIndex: matchedMemberIndex,
      },
    });

    console.log(res.data);

    if (res.status === 200) {
      alert('기록이 삭제되었습니다');

      // 기존 매칭된 멤버 리스트에서 삭제된 멤버만 제거
      const newMacthMemberList = recentUserList.filter(
        (member: any) => member.opponentId !== matchedMemberId,
      );
      setRecentUserList(newMacthMemberList);
    }
  };

  // 채팅 토글
  const handleTogleChatting = () => {
    setSendBtn((prev) => !prev);
  };

  return (
    <div
      className="recent-match-avartar"
      role="presentation"
      onFocus={handleFocus}
      onMouseOver={handleMouseOver}
      onBlur={handleBlur}
      onMouseOut={handleMouseOut}
    >
      <button className={`close ${isHover ? 'on-mouse' : ''}`} onClick={handleDeleteMatchMember}>
        <span className="material-symbols-outlined">close</span>
      </button>
      <img className="profile" src={src} alt="아바타 이미지" />
      <button className="chat" onClick={handleTogleChatting}>
        {!sendBtn ? (
          <span className="material-symbols-outlined">send</span>
        ) : (
          <span className="material-symbols-outlined">cancel_schedule_send</span>
        )}
      </button>
      {sendBtn && <MatchChatRoom chattingMemberId={matchedMemberId} setSendBtn={setSendBtn} />}
    </div>
  );
}

export default RecentMatchAvartar;
