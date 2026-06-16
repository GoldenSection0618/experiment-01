import { defineStore } from 'pinia';

export const useUserStore = defineStore('user', {
  state: () => ({
    token: '',
    userInfo: null,
    role: ''
  }),
  actions: {
    setUser({ token = '', userInfo = null, role = '' }) {
      this.token = token;
      this.userInfo = userInfo;
      this.role = role;
    },
    clearUser() {
      this.token = '';
      this.userInfo = null;
      this.role = '';
    }
  }
});
