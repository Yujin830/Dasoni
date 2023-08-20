import { createAsyncThunk, createSlice, PayloadAction } from '@reduxjs/toolkit';
import { WaitingMember } from '../../apis/response/waitingRoomRes';
import { persistReducer } from 'redux-persist';
import storage from 'redux-persist/lib/storage';
import { PURGE } from 'redux-persist';

export interface WaitingState {
  roomId: number;
  roomType: string;
  roomTitle: string;
  isMaster: boolean;
  ratingLimit: number;
  helpModalVisible: boolean;
  openRoomModalVisible: boolean;
  openQuickMatchingModalVisible: boolean;
  waitingRoomMemberList: WaitingMember[];
}

const initialState: WaitingState = {
  roomId: 0,
  roomType: 'private',
  roomTitle: '',
  isMaster: false,
  ratingLimit: 0,
  helpModalVisible: false,
  openRoomModalVisible: false,
  openQuickMatchingModalVisible: false,
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
    setOpenQuickMatchingModalVisible(state, action: PayloadAction<boolean>) {
      state.openQuickMatchingModalVisible = action.payload;
    },
  },
  extraReducers: (builder) => {
    builder.addCase(PURGE, () => initialState);
  },
});

export const {
  setWaitingRoomId,
  setRoomType,
  setRoomTitle,
  setRatingLimit,
  setMaster,
  setHelpModalVisible,
  setOpenRoomModalVisible,
  setWaitingMemberList,
  setOpenQuickMatchingModalVisible,
} = waitingSlice.actions;

const waitingPersistConfig = {
  key: 'waitingRoom', // localStorage key for waitingSlice
  storage, // Use localStorage as storage
};

const persistedWaitingReducer = persistReducer(waitingPersistConfig, waitingSlice.reducer);

export default persistedWaitingReducer;
// export default waitingSlice.reducer;
