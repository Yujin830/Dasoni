import { createSlice } from '@reduxjs/toolkit';

interface MeetingResult {
  ratingChange: number;
  matchMemberId: number;
}

interface MeetingSignalState {
  finalSignalReceiver: number;
  resultOfRoomMember: MeetingResult;
}

const meetingInitialState: MeetingSignalState = {
  finalSignalReceiver: 0,
  resultOfRoomMember: {
    ratingChange: 0,
    matchMemberId: 0,
  },
};

const meetingSlice = createSlice({
  name: 'meetingRoom',
  initialState: meetingInitialState,
  reducers: {
    setFinalSignalReceiver(state, action) {
      state.finalSignalReceiver = action.payload;
    },
    setRatingChange(state, action) {
      state.resultOfRoomMember.ratingChange = action.payload;
    },
    setMatchMemberId(state, action) {
      state.resultOfRoomMember.matchMemberId = action.payload;
    },
  },
});

export const { setFinalSignalReceiver, setRatingChange, setMatchMemberId } = meetingSlice.actions;

export default meetingSlice.reducer;
