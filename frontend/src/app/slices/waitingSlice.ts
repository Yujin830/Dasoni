import { createSlice, PayloadAction } from '@reduxjs/toolkit';
import { WaitingMember } from '../../apis/response/waitingRoomRes';

export interface WaitingState {
  roomId: number;
  roomType: string;
  roomTitle: string;
  isMaster: boolean;
  megiAcceptable: boolean;
  ratingLimit: number;
  helpModalVisible: boolean;
  openRoomModalVisible: boolean;
  waitingRoomMemberList: WaitingMember[];
}

const initialState: WaitingState = {
  roomId: 0,
  roomType: 'private',
  roomTitle: '',
  isMaster: false,
  megiAcceptable: false,
  ratingLimit: 0,
  helpModalVisible: false,
  openRoomModalVisible: false,
  waitingRoomMemberList: [],
};

const waitingSlice = createSlice({
  name: 'waitingRoom',
  initialState,
  reducers: {
    setWaitingRoomId(state, action) {
      state.roomId = action.payload;
    },
    setRoomType(state, action) {
      state.roomType = action.payload;
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
    setHelpModalVisible(state, action: PayloadAction<boolean>) {
      state.helpModalVisible = action.payload;
    },
    setOpenRoomModalVisible(state, action: PayloadAction<boolean>) {
      state.openRoomModalVisible = action.payload;
    },
    setWaitingMemberList(state, action) {
      state.waitingRoomMemberList = action.payload;
    },
  },
});

export const {
  setWaitingRoomId,
  setRoomType,
  setRoomTitle,
  setRatingLimit,
  setMaster,
  setMegiAcceptable,
  setHelpModalVisible,
  setOpenRoomModalVisible,
  setWaitingMemberList,
} = waitingSlice.actions;

export default waitingSlice.reducer;
