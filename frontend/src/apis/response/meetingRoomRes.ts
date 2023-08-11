export interface ResultOfMeetingMember {
  roomMemberInfo: MeetingRoomMemberInfo;
  ratingChange: number; // 레이팅 변화값
  matchMemberId: number; // 매칭된 memberId , 매칭 안되면 0
}

interface MeetingRoomMemberInfo {
  roomMemberId: number;
  member: MeetingMemberInfo;
  specialUser: boolean; // 메기 여부
  roomLeader: boolean; // 방장 여부
}

interface MeetingMemberInfo {
  memberId: number;
  nickname: string;
  rating: number; // 변경된 점수가 적용된 레이팅
  gender: string;
  job: string | null;
}
