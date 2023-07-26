import { createAsyncThunk, createSlice } from '@reduxjs/toolkit';
import axios from 'axios';
import { RootState } from '../store';
import setAuthorizationToken from '../../utils/setAuthorizationToken';

// 이 리덕스 모듈에서 관리 할 상태의 타입을 선언
export type User = {
  memberId?: number;
  id?: string;
  password?: string;
  nickname?: string;
  birth?: string;
  job?: string;
  sido?: number;
  gugun?: number;
};

// 초기상태를 선언
const initialState: User = {
  memberId: 0,
  id: '',
  password: '',
  nickname: '',
  birth: '',
  job: '',
  sido: 0,
  gugun: 0,
};

// 액션, 리듀서를 한 번에 만들어주는 createSlice 생성, export
const userSlice = createSlice({
  name: 'user',
  initialState,
  reducers: {},
  extraReducers: (builder) => {
    builder
      .addCase(setUserAsync.fulfilled, (state, action) => {
        if (action.type === 'user/SET_USER_LOGIN') {
          // 로그인 응답 처리 코드
          const { nickname } = action.payload; // 예시로 받아온 데이터 중 닉네임 정보를 가져옴
          return { ...state, nickname };
        } else if (action.type === 'user/SET_USER_SIGNUP') {
          // 회원가입 응답 처리 코드
          // 회원가입 후, 필요한 정보를 state에 반영
          return state;
        }
        return state; // 기존 상태를 그대로 반환
      })
      .addCase(modifyUserAsync.fulfilled, (state, action) => {
        return { ...state, ...action.payload };
      });
  },
});

// 유저 정보 업데이트
export const modifyUserAsync = createAsyncThunk('MODIFY_USER', async (modifyUser: User) => {
  const res = await axios.patch(`http://localhost:8080/users/${modifyUser.memberId}`, modifyUser);
  console.log(res);

  return {
    ...modifyUser,
  };
});

// 로그인 시 필요한 함수
export const setUserAsync = createAsyncThunk('SET_USER', async (user: User) => {
  // 스프링 부트 백엔드 서버의 로그인 API 엔드포인트 URL
  // const loginApiUrl = 'http://localhost:8080/login';

  // 서버로 전달할 데이터 (여기서는 id와 password만 보내는 예시)
  const requestData = {
    loginId: user.id,
    password: user.password,
  };

  // 서버에 POST 요청 보내기
  console.log('cors 테스트');
  const response = await axios.post('/login', requestData);

  // 서버로부터 받은 응답 처리 (응답 형식에 맞게 수정해야 함)
  const data = response.data;
  console.log('from 서버');
  console.log(data);

  // 서버에서 받은 토큰을 localstorage에 저장
  localStorage.setItem('jwtToken', data.token);
  // axios 호출시마다 토큰을 header에 포함하도록 설정
  setAuthorizationToken(data.token);

  // 여기서 필요에 따라 응답 데이터를 가공하여 리덕스 상태로 업데이트
  // 예시로 받아온 데이터 중 닉네임 정보를 업데이트하도록 함
  return {
    ...user,
    nickname: data.nickname,
  };
});

// 리덕스에 저장된 user 상태값을 export
export const getUserInfo = (state: RootState) => state.user;

// 로그인 reducer export
export default userSlice.reducer;
