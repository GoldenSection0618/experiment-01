import { createRouter, createWebHistory } from 'vue-router';
import HomeView from '../views/HomeView.vue';
import BookListView from '../views/BookListView.vue';
import LoginView from '../views/LoginView.vue';

const routes = [
  { path: '/', redirect: '/home' },
  { path: '/home', component: HomeView },
  { path: '/books', component: BookListView },
  { path: '/login', component: LoginView }
];

const router = createRouter({
  history: createWebHistory(),
  routes
});

export default router;
