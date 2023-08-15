import React, { useEffect, useState } from 'react';
import './RecentMatchAvartar.css';
import axios from 'axios';
import { useAppSelector } from '../../../app/hooks';

type recentMatchAvartar = {
  src: string;
  gender: string;
  matchedMemberIndex: number;
  recentUserList: never[];
  setRecentUserList: (newMemberList: never[]) => void;
};

function RecentMatchAvartar({
  src,
  gender,
  matchedMemberIndex,
  recentUserList,
  setRecentUserList,
}: recentMatchAvartar) {
  const [profileImg, setProfileImg] = useState('');
  const [isHover, setIsHover] = useState(false);
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

      const newMacthMemberList = recentUserList.filter(
        (member: any) => member.opponentId !== matchedMemberIndex,
      );

      setRecentUserList(newMacthMemberList);
    }
  };

  useEffect(() => {
    // 상대가 기본 이미지일 경우 default 이미지 적용
    if (src === 'null') {
      if (gender === 'female')
        setProfileImg('https://signiel-bucket.s3.ap-northeast-2.amazonaws.com/default_woman.jpg');
      else setProfileImg('https://signiel-bucket.s3.ap-northeast-2.amazonaws.com/default_man.jpg');
    }
  }, []);

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
      <img className="profile" src={profileImg} alt="아바타 이미지" />
      <span className="material-symbols-outlined chat">send</span>
    </div>
  );
}

export default RecentMatchAvartar;
