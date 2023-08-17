import { createSlice } from '@reduxjs/toolkit';
import { persistReducer } from 'redux-persist';
import storage from 'redux-persist/lib/storage';
import { PURGE } from 'redux-persist';

export interface MeetingState {
  roomId: number;
}

interface MeetingResult {
  ratingChange: number;
  matchMemberId: number;
}

interface MeetingSignalState {
  finalSignalReceiver: number;
  resultOfRoomMember: MeetingResult;
}

const meetingInitialState: MeetingSignalState & MeetingState = {
  roomId: 0,
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
    setMeetingRoomId(state, action) {
      state.roomId = action.payload;
    },
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
  extraReducers: (builder) => {
    builder.addCase(PURGE, () => meetingInitialState);
  },
});

export const { setMeetingRoomId, setFinalSignalReceiver, setRatingChange, setMatchMemberId } =
  meetingSlice.actions;

const meetingPersistConfig = {
  key: 'meetingRoom', // localStorage key for meetingSlice
  storage, // Use localStorage as storage
};

const persistedMeetingReducer = persistReducer(meetingPersistConfig, meetingSlice.reducer);

export default persistedMeetingReducer;
// export default meetingSlice.reducer;
