import { createSlice } from '@reduxjs/toolkit';

interface MeetingSignalState {
  finalSignalReceiver: number;
}

const meetingInitialState: MeetingSignalState = {
  finalSignalReceiver: 0,
};

const meetingSlice = createSlice({
  name: 'meetingRoom',
  initialState: meetingInitialState,
  reducers: {
    setFinalSignalReceiver(state, action) {
      state.finalSignalReceiver = action.payload;
    },
  },
});

export const { setFinalSignalReceiver } = meetingSlice.actions;

export default meetingSlice.reducer;
