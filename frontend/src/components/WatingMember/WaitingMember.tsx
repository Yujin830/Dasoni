import React from 'react';
import { useAppSelector } from '../../app/hooks';
import maleIcon from '../../assets/image/male_icon.png';
import femaleIcon from '../../assets/image/female_icon.png';
import RankAvartar from '../Avarta/RankAvartar/RackAvartar';
import './WaitingMember.css';
import { User } from '../../app/slices/user';

function WaitingMember({ memberId, nickname, point, matchCnt, gender }: User) {
  return (
    <div id="waiting-member">
      <div id="waiting-member-top">
        <img className="item" src={gender === 'male' ? maleIcon : femaleIcon} alt="성별 아이콘" />
        <p>{nickname}</p>
      </div>
      <div id="waiting-member-contents">
        <div id="profile">
          {/* TODO : store에 저장된 member 정보로 바꾸기 */}
          <RankAvartar point={1800} src={'rank_profile.png'} />
        </div>
        <div id="info-box">
          <div id="signal">
            <span className="name">Signal</span>
            <span className="info">{point === undefined ? 1800 : point}</span>
          </div>
          <div id="match">
            <span className="name">Match</span>
            <span className="info">{matchCnt === undefined ? 56 : matchCnt}</span>
          </div>
        </div>
      </div>
    </div>
  );
}

export default WaitingMember;
