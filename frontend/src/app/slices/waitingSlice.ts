import { createSlice } from '@reduxjs/toolkit';

export interface WaitingState {
  roomId: number | undefined;
  roomTitle: string | undefined;
  isMaster: boolean | undefined;
  megiAcceptable: boolean | undefined;
  ratingLimit: number | undefined;
}

const initialState: WaitingState = {
  roomId: undefined,
  roomTitle: undefined,
  isMaster: undefined,
  megiAcceptable: undefined,
  ratingLimit: undefined,
};

const waitingSlice = createSlice({
  name: 'waitingRoom',
  initialState,
  reducers: {
    setWaitingRoomId(state, action) {
      state.roomId = action.payload;
    },
    setRoomTitle(state, action) {
      state.roomTitle = action.payload;
    },
    setMaster(state, action) {
      state.isMaster = action.payload;
    },
    setMegiAcceptable(state, action) {
      state.megiAcceptable = action.payload;
    },
    setRatingLimit(state, action) {
      state.ratingLimit = action.payload;
    },
  },
});

export const { setWaitingRoomId, setRoomTitle, setRatingLimit, setMaster, setMegiAcceptable } =
  waitingSlice.actions;

export default waitingSlice.reducer;
