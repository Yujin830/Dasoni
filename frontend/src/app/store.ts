import { configureStore, ThunkAction, Action } from '@reduxjs/toolkit';
import waitingSlice from './slices/waitingSlice';
import meetingSlice from './slices/meetingSlice';
import persistedUserReducer from './slices/user';
import { persistStore } from 'redux-persist';

export const store = configureStore({
  reducer: {
    user: persistedUserReducer,
    waitingRoom: waitingSlice,
    meetingRoom: meetingSlice,
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
