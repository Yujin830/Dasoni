import React from 'react';
import maleIcon from '../../assets/image/male_icon.png';
import femaleIcon from '../../assets/image/female_icon.png';
import RankAvartar from '../Avarta/RankAvartar/RackAvartar';
import './WaitingMember.css';
import { User } from '../../app/slices/user';

function WaitingMember({ nickname, rating, matchCnt, gender, profileImageSrc }: User) {
  return (
    <div id="waiting-member">
      <div id="waiting-member-top">
        <img className="item" src={gender === 'male' ? maleIcon : femaleIcon} alt="성별 아이콘" />
        <p>{nickname}</p>
      </div>
      <div id="waiting-member-contents">
        <div id="info-box">
          <div id="signal">
            <span className="name">Signal</span>
            <span className="info">{rating}</span>
          </div>
          <div id="match">
            <span className="name">Match</span>
            <span className="info">{matchCnt}</span>
          </div>
        </div>
        <div id="profile">
          <RankAvartar
            point={rating !== undefined ? rating : 1000}
            src={profileImageSrc !== undefined ? profileImageSrc : 'rank_profile.png'}
          />
        </div>
      </div>
    </div>
  );
}

export default WaitingMember;
