export interface WaitingRoomInfoRes {
  roomId: number;
  title: string;
  malePartyMemberCount: number; // 남자 참가자 수
  femalePartyMemberCount: number; // 여자 참가자 수
  malePartyAvgRating: number; // 남자 참가자 평균 레이팅
  femalePartyAvgRating: number; // 여자 참가자 평균 레이팅
  megiAcceptable: boolean; // 메기 입장 여부
  ratingLimit: number; // 레이팅 제한
}

export interface WaitingMember {
  memberId: number;
  nickname: string;
  gender: string;
  profileImageSrc: string;
  rating: number;
  meetingCount: number;
}
