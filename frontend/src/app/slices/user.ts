import { createAsyncThunk, createSlice } from '@reduxjs/toolkit';
import axios from 'axios';
import { RootState } from '../store';
import setAuthorizationToken from '../../utils/setAuthorizationToken';

// 이 리덕스 모듈에서 관리 할 상태의 타입을 선언
export interface User {
  memberId?: number;
  loginId?: string;
  password?: string;
  nickname?: string;
  birth?: string;
  phoneNumber?: string;
  job?: string;
  siDo?: number;
  guGun?: number;
  gender?: string;
  profileImageSrc?: string;
  point?: number;
  matchCnt?: number;
}

// 초기상태를 선언
const initialState: User = {
  memberId: 0,
  loginId: '',
  password: '',
  nickname: '',
  birth: '',
  phoneNumber: '',
  job: '',
  siDo: 0,
  guGun: 0,
  profileImageSrc: '',
};

// 액션, 리듀서를 한 번에 만들어주는 createSlice 생성, export
const userSlice = createSlice({
  name: 'user',
  initialState,
  reducers: {},
  extraReducers: (builder) => {
    builder
      .addCase(loginAsync.fulfilled, (state, action) => {
        // 로그인 응답 처리 코드
        console.log('저장할 정보', action.payload);
        return { ...state, ...action.payload };
      })
      .addCase(signupAsync.fulfilled, (state, action) => {
        // 회원가입 응답 처리 코드(회원가입 후, 필요한 정보를 state에 반영)
        return { ...state, ...action.payload };
      })
      .addCase(modifyUserAsync.fulfilled, (state, action) => {
        return { ...state, ...action.payload };
      });
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
  const response = await axios.post('/api/register', requestData);

  // 서버로부터 받은 응답 처리 (응답 형식에 맞게 수정해야 함)
  const data = response.data;
  console.log('from 서버');
  console.log(data);

  // 여기서 필요에 따라 응답 데이터를 가공하여 리덕스 상태로 업데이트
  return {
    memberId: data.memberId,
    id: data.loginId,
    nickname: data.nickname,
    gender: data.gender,
    birth: data.birth,
    phoneNumber: data.phoneNumber,
    rank: data.rank,
    meetingCount: data.meetingCount,
    profileImageSrc: data.profileImageSrc,
    job: data.job,
    siDo: data.siDo,
    guGun: data.guGun,
    roles: data.roles,
    remainLife: data.remainLife,
    black: data.black,
  };
});

// 유저 정보 업데이트
export const modifyUserAsync = createAsyncThunk('MODIFY_USER', async (modifyUser: User) => {
  const requestData = {
    siDo: modifyUser.siDo,
    guGun: modifyUser.guGun,
    job: modifyUser.job,
    nickname: modifyUser.nickname,
    profileImageSrc: modifyUser.profileImageSrc,
  };
  console.log('here', modifyUser);
  const response = await axios.patch(`/api/users/${modifyUser.memberId}`, requestData);
  const data = response.data;
  console.log('from 서버');
  console.log(data);

  return {
    siDo: data.siDo,
    guGun: data.guGun,
    job: data.job,
    nickname: data.nickname,
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

    // 서버에서 받은 토큰을 localstorage에 저장
    localStorage.setItem('jwtToken', data.token);
    // axios 호출시마다 토큰을 header에 포함하도록 설정
    setAuthorizationToken(data.token);

    // 여기서 필요에 따라 응답 데이터를 가공하여 리덕스 상태로 업데이트
    return {
      memberId: data.memberId,
      loginId: data.loginId,
      nickname: data.nickname,
      gender: data.gender,
      birth: data.birth,
      phoneNumber: data.phoneNumber,
      rank: data.rank,
      meetingCount: data.meetingCount,
      profileImageSrc: data.profileImageSrc,
      job: data.job,
      siDo: data.siDo,
      guGun: data.guGun,
      roles: data.roles,
      remainLife: data.remainLife,
      // token: data.token,
      black: data.black,
    };
  } catch (error) {
    // 로그인에 실패한 경우 에러를 던집니다.
    throw new Error('인증 정보가 올바르지 않습니다.');
  }
});
// 로그아웃 시 필요한 함수
export const logout = () => {
  // 로컬 스토리지에서 토큰 삭제
  localStorage.removeItem('jwtToken');
  // Axios 헤더에서 인증 토큰 제거
  setAuthorizationToken(null); // Axios 헤더에서 토큰을 null로 설정하는 함수를 가정합니다.
  console.log('로그아웃 성공?', localStorage);
  // 필요에 따라 Redux 상태를 초기화하는 액션을 디스패치할 수도 있습니다.
  // dispatch(resetUserState());
};

// 리덕스에 저장된 user 상태값을 export
export const getUserInfo = (state: RootState) => state.user;

// 로그인 reducer export
export default userSlice.reducer;
