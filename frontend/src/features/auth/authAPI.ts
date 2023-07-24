import axios from 'axios';

export const login = async (credentials: { username: string; password: string }) => {
  const response = await axios.post('/http://localhost:8080/users/login', credentials);
  return response.data;
};

export const signup = async (userInfo: any) => {
  const response = await axios.post('/http://localhost:8080/users/regist', userInfo);
  return response.data;
};
