import React, { useState, useEffect } from 'react';
import { useAppSelector } from '../../app/hooks';
import RecentMatchAvartar from '../../components/Avarta/RecentMatchAvartar/RecentMatchAvartar';
import RankAvartar from '../Avarta/RankAvartar/RackAvartar';
import ExpPointBar from '../Element/ExpPointBar';
import user, { deleteUserAsync } from '../../app/slices/user';
import { useAppDispatch } from '../../app/hooks';
import { useNavigate } from 'react-router';
import axios from 'axios';

function MyProfile({ setType }: { setType: (type: string) => void }) {
  const { memberId, loginId, nickname, job, birth, siDo, guGun, gender } = useAppSelector(
    (state) => state.user,
  );
  console.log(siDo, guGun);
  const user = useAppSelector((state) => state.user);
  const [recentUserList, setRecentUserList] = useState<any[]>([]);
  const dispatch = useAppDispatch();
  const navigate = useNavigate();
  const deleteUser = async (e: React.MouseEvent<HTMLAnchorElement>) => {
    e.preventDefault();
    if (window.confirm(`탈퇴하면 복구할 수 없습니다.\n정말 탈퇴하시겠습니까?`)) {
      try {
        // TODO : 회원 탈퇴
        await dispatch(deleteUserAsync(user));
        alert(`탈퇴 되었습니다.`);
        // location : 로그인 화면으로 이동
        navigate('/');
      } catch (error) {
        console.error(error);
        alert(`탈퇴 중에 오류가 발생했습니다.`);
      }
    }
  };

  const modifyUser = (e: React.MouseEvent<HTMLAnchorElement>) => {
    e.preventDefault();
    setType('modify');
  };

  const changePw = (e: React.MouseEvent<HTMLAnchorElement>) => {
    e.preventDefault();
    setType('changePw');
  };

  useEffect(() => {
    getRecentMatchedMemberList();
  }, []);

  // 최근 매칭된 다소니 리스트 조회
  const getRecentMatchedMemberList = async () => {
    const res = await axios.get(`/api/users/${memberId}/history`);
    console.log(res.data);

    if (res.status === 200) {
      setRecentUserList(res.data.content);
    }
  };

  return (
    <div className="content">
      <p id="user">
        <span id="name">
          {nickname}({loginId})
        </span>
        님의 개인정보
      </p>
      <table>
        <tbody>
          <tr>
            <td className="name">생년월일</td>
            <td className="info">{birth}</td>
          </tr>
          <tr>
            <td className="name">주소</td>
            <td className="info">
              {siDo} {guGun}
            </td>
          </tr>
          <tr>
            <td className="name">직업</td>
            <td className="info">{job}</td>
          </tr>
        </tbody>
      </table>
      <div id="recent-matched-user-box">
        <div className="recent-matched-title">
          <p>최근 매칭된 다소니</p>
        </div>
        <div id="matched-user-list">
          {recentUserList.length > 0 ? (
            recentUserList.map((member: any, index) => (
              <RecentMatchAvartar
                key={member.opponentId}
                matchedMemberId={member.opponentId}
                src={member.profileImageUrl}
                matchedMemberIndex={index}
                gender={gender === 'male' ? 'female' : 'male'}
                recentUserList={recentUserList}
                setRecentUserList={setRecentUserList}
              />
            ))
          ) : (
            <p>최근 매칭된 다소니가 없습니다</p>
          )}
        </div>
      </div>
      <footer>
        <a className="btn mobile" href="/" onClick={changePw}>
          비밀번호 변경
        </a>
        <a className="btn modify" href="/" onClick={modifyUser}>
          회원 정보 수정
        </a>
        <a className="btn remove" href="/" onClick={deleteUser}>
          회원 탈퇴
        </a>
      </footer>
    </div>
  );
}

export default MyProfile;
