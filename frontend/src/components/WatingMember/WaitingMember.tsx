import React from 'react';
import { useAppSelector } from '../../app/hooks';
import maleIcon from '../../assets/image/male_icon.png';
import femaleIcon from '../../assets/image/female_icon.png';
import RankAvartar from '../Avarta/RankAvartar/RackAvartar';
import './WaitingMember.css';

function WaitingMember() {
  const member = useAppSelector((state) => state.user);
  return (
    <div id="waiting-member">
      <div id="waiting-member-top">
        <img
          className="item"
          src={member.gender === 'male' ? maleIcon : femaleIcon}
          alt="성별 아이콘"
        />
        <p>{member.nickname === '' ? 'sunny' : member.nickname}</p>
      </div>
      <div id="waiting-member-contents">
        <div id="profile">
          {/* TODO : store에 저장된 member 정보로 바꾸기 */}
          <RankAvartar point={1800} src={'rank_profile.png'} />
        </div>
        <div id="info-box">
          <div id="signal">
            <span className="name">Signal</span>
            <span className="info">{member.point === undefined ? 1800 : member.point}</span>
          </div>
          <div id="match">
            <span className="name">Match</span>
            <span className="info">{member.matchCnt === undefined ? 56 : member.matchCnt}</span>
          </div>
        </div>
      </div>
    </div>
  );
}

export default WaitingMember;
