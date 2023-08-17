import { configureStore, ThunkAction, Action } from '@reduxjs/toolkit';
import persistedUserReducer from './slices/user';
import { persistStore } from 'redux-persist';
import persistedWaitingReducer from './slices/waitingSlice';
import persistedMeetingReducer from './slices/meetingSlice';

export const store = configureStore({
  reducer: {
    user: persistedUserReducer,
    waitingRoom: persistedWaitingReducer,
    meetingRoom: persistedMeetingReducer,
  },
});

export const persistor = persistStore(store);

export type AppDispatch = typeof store.dispatch;
export type RootState = ReturnType<typeof store.getState>;
export default store;
export type AppThunk<ReturnType = void> = ThunkAction<
  ReturnType,
  RootState,
  unknown,
  Action<string>
>;
