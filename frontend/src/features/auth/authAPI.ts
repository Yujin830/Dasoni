import axios from 'axios';

export const login = async (credentials: { username: string; password: string }) => {
  const response = await axios.post('/http://localhost:8080/login', credentials);
  return response.data;
};

export const signup = async (userInfo: any) => {
  const response = await axios.post('/http://localhost:8080/regist', userInfo);
  return response.data;
};
