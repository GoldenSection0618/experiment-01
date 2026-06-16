<template>
  <el-container class="app-shell">
    <el-header class="app-header">
      <div class="brand">校园图书借阅自助管理系统</div>
      <el-menu :default-active="activePath" mode="horizontal" router class="nav-menu">
        <el-menu-item index="/home">首页</el-menu-item>
        <el-menu-item index="/books">图书列表</el-menu-item>
        <el-menu-item index="/login">登录占位</el-menu-item>
      </el-menu>
    </el-header>

    <el-main class="app-main">
      <router-view />
    </el-main>
  </el-container>
</template>

<script setup>
import { computed } from 'vue';
import { useRoute } from 'vue-router';

const route = useRoute();
const activePath = computed(() => route.path);
</script>
