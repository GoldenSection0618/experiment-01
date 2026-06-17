import { createRouter, createWebHistory } from 'vue-router';
import HomeView from '../views/HomeView.vue';
import BookListView from '../views/BookListView.vue';
import LoginView from '../views/LoginView.vue';
import RegisterView from '../views/RegisterView.vue';
import PasswordView from '../views/PasswordView.vue';
import MyBorrowsView from '../views/MyBorrowsView.vue';
import AdminHomeView from '../views/admin/AdminHomeView.vue';
import CategoryManageView from '../views/admin/CategoryManageView.vue';
import BookManageView from '../views/admin/BookManageView.vue';
import UserManageView from '../views/admin/UserManageView.vue';
import BorrowManageView from '../views/admin/BorrowManageView.vue';
import StatisticsView from '../views/admin/StatisticsView.vue';
import { useUserStore } from '../stores/user';

const routes = [
  { path: '/', redirect: '/home' },
  { path: '/home', component: HomeView, meta: { requiresAuth: true } },
  { path: '/books', component: BookListView, meta: { requiresAuth: true } },
  { path: '/my-borrows', component: MyBorrowsView, meta: { requiresAuth: true, roles: ['USER'] } },
  { path: '/password', component: PasswordView, meta: { requiresAuth: true } },
  { path: '/admin', component: AdminHomeView, meta: { requiresAuth: true, roles: ['ADMIN'] } },
  { path: '/admin/categories', component: CategoryManageView, meta: { requiresAuth: true, roles: ['ADMIN'] } },
  { path: '/admin/books', component: BookManageView, meta: { requiresAuth: true, roles: ['ADMIN'] } },
  { path: '/admin/users', component: UserManageView, meta: { requiresAuth: true, roles: ['ADMIN'] } },
  { path: '/admin/borrows', component: BorrowManageView, meta: { requiresAuth: true, roles: ['ADMIN'] } },
  { path: '/admin/statistics', component: StatisticsView, meta: { requiresAuth: true, roles: ['ADMIN'] } },
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
