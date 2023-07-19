import { createAsyncThunk, createSlice } from '@reduxjs/toolkit';
import { RootState } from '../store';

// 이 리덕스 모듈에서 관리 할 상태의 타입을 선언
export type User = {
  id: string;
  password: string;
};

// 초기상태를 선언
const initialState: User = {
  id: '',
  password: '',
};

// 액션, 리듀서를 한 번에 만들어주는 createSlice 생성, export
const userSlice = createSlice({
  name: 'user',
  initialState,
  reducers: {},
  extraReducers: (builder) => {
    builder.addCase(setUserAsync.fulfilled, (state, action) => {
      return { ...state, ...action.payload };
    });
  },
});

// 로그인 시 필요한 함수
export const setUserAsync = createAsyncThunk('SET_USER', async (user: User) => {
  return user;
});

// 리덕스에 저장된 user 상태값을 export
export const getUserInfo = (state: RootState) => state.user;

// 로그인 reducer export
export default userSlice.reducer;
