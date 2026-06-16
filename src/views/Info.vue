<template>
  <app-layout>
    <div class="info-page">
      <div class="page-header">
        <h2 class="page-title"><i class="el-icon-info"></i>信息页面</h2>
        <el-breadcrumb separator="/">
          <el-breadcrumb-item :to="{ path: '/home' }">主页</el-breadcrumb-item>
          <el-breadcrumb-item>信息页面</el-breadcrumb-item>
        </el-breadcrumb>
      </div>

      <el-card class="section-card" shadow="never">
        <div slot="header" class="card-header">
          <i class="el-icon-user"></i>
          <span>个人信息</span>
        </div>
        <div v-if="user" class="profile">
          <div class="avatar-circle"><i class="el-icon-user-solid"></i></div>
          <div class="profile-grid">
            <div v-for="item in fields" :key="item.key" class="kv">
              <span class="kv-label">{{ item.label }}</span>
              <span class="kv-value">{{ user[item.key] }}{{ item.suffix || '' }}</span>
            </div>
          </div>
        </div>
        <el-empty v-else description="请先登录" />
      </el-card>

      <div class="actions">
        <el-button type="primary" icon="el-icon-s-home" @click="$router.push('/home')">返回主页</el-button>
        <el-button icon="el-icon-user" @click="$router.push('/users')">用户管理</el-button>
      </div>
    </div>
  </app-layout>
</template>

<script>
import AppLayout from '../components/AppLayout.vue';
import { getUser } from '../utils/session';

export default {
  name: 'InfoPage',
  components: { AppLayout },
  data() {
    return {
      user: null,
      fields: [
        { label: '姓名', key: 'name' },
        { label: '用户名', key: 'username' },
        { label: '性别', key: 'gender' },
        { label: '年龄', key: 'age', suffix: ' 岁' },
        { label: '邮箱', key: 'email' },
        { label: '出生年月', key: 'birthday' },
        { label: '地址', key: 'address' },
        { label: '注册时间', key: 'registerTime' }
      ]
    };
  },
  created() {
    this.user = getUser();
    if (!this.user) {
      this.$message.warning('请先登录');
      this.$router.replace('/login');
    }
  }
};
</script>

<style scoped>
.info-page {
  max-width: 1000px;
}

.section-card {
  margin-bottom: 18px;
}

.profile {
  display: flex;
  gap: 32px;
  align-items: flex-start;
}

.avatar-circle {
  width: 110px;
  height: 110px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  background: var(--primary-light, #ecf5ff);
  color: var(--primary-color, #409EFF);
}

.avatar-circle i {
  font-size: 52px;
}

.profile-grid {
  flex: 1;
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 12px;
}

.kv {
  display: flex;
  padding: 10px 12px;
  border-bottom: 1px solid #ebeef5;
}

.kv-label {
  min-width: 80px;
  color: #909399;
}

.kv-value {
  color: #303133;
}

.actions {
  display: flex;
  gap: 12px;
}
</style>
