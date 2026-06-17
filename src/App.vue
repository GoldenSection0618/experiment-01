<template>
  <el-container class="app-shell">
    <el-header class="app-header">
      <div class="brand">校园图书借阅自助管理系统</div>
      <el-menu :default-active="activePath" mode="horizontal" router class="nav-menu">
        <template v-if="userStore.isLoggedIn">
          <el-menu-item index="/home">首页</el-menu-item>
          <el-menu-item index="/books">图书列表</el-menu-item>
          <el-menu-item v-if="userStore.role === 'USER'" index="/my-borrows">我的借阅</el-menu-item>
          <template v-if="userStore.role === 'ADMIN'">
            <el-menu-item index="/admin">管理首页</el-menu-item>
            <el-menu-item index="/admin/categories">分类管理</el-menu-item>
            <el-menu-item index="/admin/books">图书管理</el-menu-item>
            <el-menu-item index="/admin/users">用户管理</el-menu-item>
            <el-menu-item index="/admin/borrows">借阅记录</el-menu-item>
            <el-menu-item index="/admin/statistics">统计分析</el-menu-item>
          </template>
          <el-menu-item index="/password">修改密码</el-menu-item>
        </template>
        <template v-else>
          <el-menu-item index="/login">登录</el-menu-item>
          <el-menu-item index="/register">注册</el-menu-item>
        </template>
      </el-menu>
      <el-button v-if="userStore.isLoggedIn" type="primary" plain @click="logout">退出登录</el-button>
    </el-header>

    <el-main class="app-main">
      <router-view />
    </el-main>
  </el-container>
</template>

<script setup>
import { computed } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { useUserStore } from './stores/user';

const route = useRoute();
const router = useRouter();
const userStore = useUserStore();
userStore.loadFromStorage();
const activePath = computed(() => route.path);

function logout() {
  userStore.clearLoginInfo();
  router.push('/login');
}
</script>
