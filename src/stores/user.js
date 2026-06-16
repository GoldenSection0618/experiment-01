import { defineStore } from 'pinia';

export const useUserStore = defineStore('user', {
  state: () => ({
    token: '',
    userInfo: null,
    role: ''
  }),
  getters: {
    isLoggedIn: state => Boolean(state.token)
  },
  actions: {
    setLoginInfo({ token = '', userInfo = null, role = '' }) {
      this.token = token;
      this.userInfo = userInfo;
      this.role = role;
      localStorage.setItem('library_user', JSON.stringify({ token, userInfo, role }));
    },
    loadFromStorage() {
      const saved = localStorage.getItem('library_user');
      if (!saved) {
        return;
      }
      try {
        const data = JSON.parse(saved);
        this.token = data.token || '';
        this.userInfo = data.userInfo || null;
        this.role = data.role || '';
      } catch (error) {
        this.clearLoginInfo();
      }
    },
    clearLoginInfo() {
      this.token = '';
      this.userInfo = null;
      this.role = '';
      localStorage.removeItem('library_user');
    },
    setUser(data) {
      this.setLoginInfo(data);
    },
    clearUser() {
      this.clearLoginInfo();
    }
  }
});
