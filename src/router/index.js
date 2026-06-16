import { createRouter, createWebHistory } from 'vue-router';
import HomeView from '../views/HomeView.vue';
import BookListView from '../views/BookListView.vue';
import LoginView from '../views/LoginView.vue';
import RegisterView from '../views/RegisterView.vue';
import PasswordView from '../views/PasswordView.vue';
import AdminView from '../views/AdminView.vue';
import { useUserStore } from '../stores/user';

const routes = [
  { path: '/', redirect: '/home' },
  { path: '/home', component: HomeView, meta: { requiresAuth: true } },
  { path: '/books', component: BookListView, meta: { requiresAuth: true } },
  { path: '/password', component: PasswordView, meta: { requiresAuth: true } },
  { path: '/admin', component: AdminView, meta: { requiresAuth: true, roles: ['ADMIN'] } },
  { path: '/login', component: LoginView },
  { path: '/register', component: RegisterView }
];

const router = createRouter({
  history: createWebHistory(),
  routes
});

router.beforeEach(to => {
  const userStore = useUserStore();
  userStore.loadFromStorage();

  if ((to.path === '/login' || to.path === '/register') && userStore.isLoggedIn) {
    return '/home';
  }

  if (to.meta.requiresAuth && !userStore.isLoggedIn) {
    return { path: '/login', query: { redirect: to.fullPath } };
  }

  if (to.meta.roles && !to.meta.roles.includes(userStore.role)) {
    return '/home';
  }

  return true;
});

export default router;
