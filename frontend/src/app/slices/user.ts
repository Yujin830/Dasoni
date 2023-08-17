import { createAsyncThunk, createSlice } from '@reduxjs/toolkit';
import axios from 'axios';
import { RootState } from '../store';
import setAuthorizationToken from '../../utils/setAuthorizationToken';
import { ActivationState } from '@stomp/stompjs';
import { persistReducer } from 'redux-persist';
import storage from 'redux-persist/lib/storage';
import { PURGE } from 'redux-persist';
import { persistor } from '../store';

// 이 리덕스 모듈에서 관리 할 상태의 타입을 선언
export interface User {
  memberId?: number;
  loginId?: string;
  password?: string;
  nickname?: string;
  birth?: string;
  phoneNumber?: string;
  job?: string;
  siDo?: number | string;
  guGun?: number | string;
  gender?: string;
  profileImageSrc?: string;
  rating?: number;
  matchCnt?: number;
  isFirst?: number;
  remainLife?: number;
  profileImage?: File | null;
}

// 초기상태를 선언
const initialState: User = {
  memberId: 0,
  loginId: '',
  password: '',
  nickname: '닉네임',
  birth: '',
  phoneNumber: '',
  job: '선택해 주세요',
  siDo: '선택해',
  guGun: '주세요',
  profileImageSrc: '',
  isFirst: 0,
  rating: 1000,
  matchCnt: 0,
  remainLife: 2,
};

// 액션, 리듀서를 한 번에 만들어주는 createSlice 생성, export
const userSlice = createSlice({
  name: 'user',
  initialState,
  reducers: {
    setSido(state, action) {
      state.siDo = action.payload;
    },
    setGugun(state, action) {
      state.guGun = action.payload;
    },
    setRating(state, action) {
      state.rating = action.payload;
    },
    setRemainLife(state, action) {
      state.remainLife = action.payload;
    },
    setMeetingCount(state, action) {
      state.matchCnt = action.payload;
    },
    setProfileImageSrc(state, action) {
      state.profileImageSrc = action.payload;
    },
  },
  extraReducers: (builder) => {
    builder
      .addCase(loginAsync.fulfilled, (state, action) => {
        // 로그인 응답 처리 코드
        console.log(action.payload);
        return { ...state, ...action.payload };
      })
      .addCase(modifyUserAsync.fulfilled, (state, action) => {
        console.log(action.payload);
        return { ...state, ...action.payload };
      })
      .addCase(PURGE, () => initialState);
  },
});

// 회원가입시 필요한 함수
export const signupAsync = createAsyncThunk('user/SIGNUP', async (user: User) => {
  // 서버로 전달할 회원가입 데이터
  const requestData = {
    loginId: user.loginId,
    password: user.password,
    nickname: user.nickname,
    gender: user.gender,
    birth: user.birth,
    phoneNumber: user.phoneNumber,
    // 기타 회원가입에 필요한 데이터들을 추가로 넣어주세요
  };

  // 서버에 POST 요청 보내기
  await axios.post('/api/register', requestData);
});

// 회원 탈퇴
export const deleteUserAsync = createAsyncThunk('DELETE_USER', async (user: User) => {
  const response = await axios.delete(`/api/users/${user.memberId}`);
  console.log('회원탈퇴');
  console.log(response);
  localStorage.removeItem('jwtToken');
  return {};
});

// 유저 정보 업데이트
export const modifyUserAsync = createAsyncThunk('MODIFY_USER', async (modifyUser: User) => {
  const requestData = {
    profileImageSrc: modifyUser.profileImageSrc,
    siDo: modifyUser.siDo,
    guGun: modifyUser.guGun,
    job: modifyUser.job,
    nickname: modifyUser.nickname,
  };
  console.log(requestData);

  const fd = new FormData();
  if (modifyUser.profileImage !== null && modifyUser.profileImage !== undefined) {
    fd.append('file', modifyUser.profileImage);
  }
  fd.append('key', new Blob([JSON.stringify(requestData)], { type: 'application/json' }));

  console.log('here', modifyUser);
  const response = await axios.patch(`/api/users/${modifyUser.memberId}`, fd, {
    headers: {
      'Content-Type': `multipart/form-data`,
    },
  });
  const data = response.data;
  console.log('from 서버');
  console.log(data);

  return {
    // siDo: modifyUser.siDo,
    // guGun: modifyUser.guGun,
    job: modifyUser.job,
    nickname: modifyUser.nickname,
    profileImageSrc: data,
  };
});

// 로그인 시 필요한 함수
export const loginAsync = createAsyncThunk('user/LOGIN', async (user: User) => {
  const requestData = {
    loginId: user.loginId,
    password: user.password,
  };

  try {
    // 서버에 POST 요청 보내기
    const response = await axios.post('/api/login', requestData);

    // 서버로부터 받은 응답 처리 (응답 형식에 맞게 수정해야 함)
    const data = response.data;
    console.log('from 서버');
    console.log(data);
    localStorage.removeItem('jwtToken');
    // 서버에서 받은 토큰을 localstorage에 저장
    localStorage.setItem('jwtToken', data.token);
    // axios 호출시마다 토큰을 header에 포함하도록 설정
    setAuthorizationToken(data.token);

    // 여기서 필요에 따라 응답 데이터를 가공하여 리덕스 상태로 업데이트
    return {
      memberId: data.memberId,
      loginId: data.loginId,
      nickname: data.nickname || '닉네임',
      gender: data.gender,
      birth: data.birth,
      phoneNumber: data.phoneNumber,
      meetingCount: data.meetingCount || 0,
      profileImageSrc: data.profileImageSrc,
      job: data.job || '선택해 주세요',
      siDo: data.siDo || '선택해',
      guGun: data.guGun || '주세요',
      // roles: data.roles,
      remainLife: data.remainLife,
      rating: data.rating || 1000,
      // token: data.token,
      black: data.black,
      isFirst: data.isFirst,
    };
  } catch (error) {
    // 로그인에 실패한 경우 에러를 던집니다.
    throw new Error('인증 정보가 올바르지 않습니다.');
  }
});

// 로그아웃 시 필요한 함수
export const logout = async () => {
  localStorage.removeItem('jwtToken');
  localStorage.clear();
  setAuthorizationToken(null); // Axios 헤더에서 토큰을 null로 설정하는 함수를 가정합니다.
  await persistor.purge(); // store 제거
  console.log('로그아웃', localStorage);
  // dispatch(resetUserState());
};

// 리덕스에 저장된 user 상태값을 export
export const getUserInfo = (state: RootState) => state.user;

export const { setSido, setGugun, setRating, setRemainLife, setMeetingCount, setProfileImageSrc } =
  userSlice.actions;

// persistReducer를 사용하여 redux-persist 적용
const persistConfig = {
  key: 'user', // localStorage key
  storage, // localStorage
};
const persistedUserReducer = persistReducer(persistConfig, userSlice.reducer);

export default persistedUserReducer;
