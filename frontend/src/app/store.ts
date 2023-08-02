import { configureStore, ThunkAction, Action } from '@reduxjs/toolkit';
import userReducer from './slices/user';
import waitingReducer from './slices/waitingSlice';

export const store = configureStore({
  reducer: {
    user: userReducer,
    waitingRoom: waitingReducer,
  },
});

export type AppDispatch = typeof store.dispatch;
export type RootState = ReturnType<typeof store.getState>;
export default store;
export type AppThunk<ReturnType = void> = ThunkAction<
  ReturnType,
  RootState,
  unknown,
  Action<string>
>;
