<template>
  <el-header class="app-header">
    <div class="header-left">
      <el-button class="menu-toggle-btn" type="text" icon="el-icon-s-fold" @click="$emit('toggle-menu')" />
      <span class="header-title">实验一 · 静态网页设计</span>
    </div>
    <div class="header-right">
      <el-badge :value="3" class="header-badge">
        <i class="el-icon-bell header-icon"></i>
      </el-badge>
      <el-dropdown trigger="click" @command="handleCommand">
        <span class="header-user">
          <i class="el-icon-user-solid"></i>
          <span>{{ user.name }}</span>
          <i class="el-icon-arrow-down"></i>
        </span>
        <el-dropdown-menu slot="dropdown">
          <el-dropdown-item command="info">个人信息</el-dropdown-item>
          <el-dropdown-item command="password">修改密码</el-dropdown-item>
          <el-dropdown-item divided command="logout">退出登录</el-dropdown-item>
        </el-dropdown-menu>
      </el-dropdown>
    </div>
  </el-header>
</template>

<script>
import { currentUser } from '../data/users';

export default {
  name: 'AppHeader',
  data() {
    return {
      user: currentUser
    };
  },
  methods: {
    handleCommand(cmd) {
      if (cmd === 'logout') {
        this.$message.success('已退出登录');
        this.$router.push('/login');
      } else if (cmd === 'info') {
        this.$router.push('/info');
      } else if (cmd === 'password') {
        this.$router.push('/forgot-password');
      }
    }
  }
};
</script>

<style scoped>
.app-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 24px;
  height: 60px !important;
  z-index: 1001;
  background-color: var(--header-bg, #304156);
  color: var(--header-text, #fff);
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.15);
}
.header-left  { display: flex; align-items: center; gap: 12px; }
.header-right { display: flex; align-items: center; gap: 20px; }
.header-title { font-size: 18px; font-weight: 600; letter-spacing: 1px; white-space: nowrap; }
.header-icon  { font-size: 20px; color: #fff; }
.header-badge { cursor: pointer; }
.header-user {
  display: flex;
  align-items: center;
  gap: 6px;
  cursor: pointer;
  padding: 6px 12px;
  border-radius: 4px;
  transition: background 0.2s;
  color: #fff;
}
.header-user:hover { background: rgba(255, 255, 255, 0.15); }
.menu-toggle-btn {
  color: #fff !important;
  font-size: 20px;
  padding: 4px 8px;
  display: none;
}
</style>
