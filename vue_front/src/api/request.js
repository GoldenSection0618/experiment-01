import axios from 'axios';
import router from '../router';
import { useUserStore } from '../stores/user';

const request = axios.create({
  baseURL: '/api',
  timeout: 8000
});

request.interceptors.request.use(config => {
  const userStore = useUserStore();
  userStore.loadFromStorage();
  if (userStore.token) {
    config.headers.Authorization = `Bearer ${userStore.token}`;
  }
  return config;
});

request.interceptors.response.use(
  response => response.data,
  error => {
    if (error.response && error.response.status === 401) {
      const userStore = useUserStore();
      userStore.clearLoginInfo();
      if (router.currentRoute.value.path !== '/login') {
        router.push('/login');
      }
    }
    return Promise.reject(error);
  }
);

export default request;
