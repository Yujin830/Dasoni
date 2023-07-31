export interface WaitingRoomInfoRes {
  roomId: number;
  title: string;
  maleCnt: number;
  femaleCnt: number;
  isMegiOpen: boolean;
  rank: number;
}

export interface WaitingMember {
  memberId: number;
  nickname: string;
  gender: string;
  profileSrc: string;
  points: number;
  matchCnt: number;
}
