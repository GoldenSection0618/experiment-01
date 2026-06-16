import Vue from 'vue';
import VueRouter from 'vue-router';
import Login from '../views/Login.vue';
import Register from '../views/Register.vue';
import ForgotPassword from '../views/ForgotPassword.vue';
import Home from '../views/Home.vue';
import Info from '../views/Info.vue';
import UserList from '../views/UserList.vue';

Vue.use(VueRouter);

const routes = [
  { path: '/', redirect: '/login' },
  { path: '/login', component: Login, meta: { title: '登录' } },
  { path: '/register', component: Register, meta: { title: '注册' } },
  { path: '/forgot-password', component: ForgotPassword, meta: { title: '忘记密码' } },
  { path: '/home', component: Home, meta: { title: '主页' } },
  { path: '/info', component: Info, meta: { title: '信息页面' } },
  { path: '/users', component: UserList, meta: { title: '用户列表' } }
];

const router = new VueRouter({
  mode: 'hash',
  routes
});

export default router;
