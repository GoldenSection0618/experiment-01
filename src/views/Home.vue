<template>
  <app-layout>
    <div class="home-content">
      <section class="welcome-bar">
        <div>
          <h2>欢迎，{{ user ? user.name : '未登录用户' }}</h2>
          <p>{{ currentDate }}</p>
        </div>
        <el-button type="primary" plain @click="$router.push('/info')">查看个人信息</el-button>
      </section>

      <section class="image-row">
        <div class="img-card">
          <i class="el-icon-picture-outline"></i>
          <span>图片展示区</span>
        </div>
        <div class="profile-card">
          <strong>{{ user ? user.name : '请先登录' }}</strong>
          <p>{{ user ? user.email : '暂无邮箱' }}</p>
          <p>{{ user ? user.address : '暂无地址' }}</p>
        </div>
      </section>

      <section class="home-grid">
        <el-card shadow="never">
          <div slot="header" class="card-header">
            <i class="el-icon-date"></i>
            <span>日历</span>
          </div>
          <el-calendar v-model="calendarValue" />
        </el-card>

        <el-card shadow="never">
          <div slot="header" class="card-header">
            <i class="el-icon-document"></i>
            <span>页面说明</span>
          </div>
          <p class="simple-text">这是一个使用 Vue、ElementUI 和 SpringBoot 完成的前后端分离 Web 系统。</p>
          <div class="home-actions">
            <el-button size="small" @click="$router.push('/users')">用户管理</el-button>
            <el-button size="small" @click="$router.push('/forgot-password')">修改密码</el-button>
          </div>
        </el-card>
      </section>
    </div>
  </app-layout>
</template>

<script>
import AppLayout from '../components/AppLayout.vue';
import { getUser } from '../utils/session';

export default {
  name: 'HomePage',
  components: { AppLayout },
  data() {
    return {
      user: null,
      calendarValue: new Date()
    };
  },
  created() {
    this.user = getUser();
    if (!this.user) {
      this.$message.warning('请先登录');
      this.$router.replace('/login');
    }
  },
  computed: {
    currentDate() {
      const d = new Date();
      const weekdays = ['日', '一', '二', '三', '四', '五', '六'];
      return `${d.getFullYear()}年${d.getMonth() + 1}月${d.getDate()}日 星期${weekdays[d.getDay()]}`;
    }
  }
};
</script>

<style scoped>
.home-content {
  max-width: 1200px;
}

.welcome-bar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  background: #fff;
  border-left: 4px solid var(--primary-color, #409EFF);
  padding: 22px 26px;
  margin-bottom: 18px;
}

.welcome-bar h2 {
  font-size: 24px;
  margin-bottom: 6px;
}

.welcome-bar p,
.simple-text,
.profile-card p {
  color: #606266;
}

.image-row {
  display: grid;
  grid-template-columns: 2fr 1fr;
  gap: 16px;
  margin-bottom: 18px;
}

.img-card,
.profile-card {
  min-height: 150px;
  background: #fff;
  border: 1px solid #ebeef5;
  display: flex;
  flex-direction: column;
  justify-content: center;
  padding: 24px;
}

.img-card {
  align-items: center;
  gap: 10px;
  color: var(--primary-color, #409EFF);
}

.img-card i {
  font-size: 42px;
}

.profile-card {
  gap: 8px;
}

.profile-card strong {
  font-size: 20px;
}

.home-grid {
  display: grid;
  grid-template-columns: 1.4fr 1fr;
  gap: 16px;
}

.home-actions {
  margin-top: 16px;
  display: flex;
  gap: 10px;
}
</style>
