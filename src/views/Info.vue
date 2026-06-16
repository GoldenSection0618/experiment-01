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

      <el-card class="section-card" shadow="hover">
        <div slot="header" class="card-header">
          <i class="el-icon-user"></i>
          <span>个人信息</span>
        </div>
        <div class="profile">
          <div class="profile-side">
            <div class="avatar-circle"><i class="el-icon-user-solid"></i></div>
            <strong>{{ userInfo.name }}</strong>
            <el-tag size="small" :type="userInfo.status === '在线' ? 'success' : 'info'">
              {{ userInfo.status }}
            </el-tag>
          </div>
          <div class="profile-grid">
            <div class="kv" v-for="item in fields" :key="item.label">
              <span class="kv-label">{{ item.label }}</span>
              <span class="kv-value">{{ userInfo[item.key] }}{{ item.suffix || '' }}</span>
            </div>
          </div>
        </div>
      </el-card>

      <el-card class="section-card" shadow="hover">
        <div slot="header" class="card-header">
          <i class="el-icon-s-tools"></i>
          <span>系统说明</span>
        </div>
        <el-row :gutter="20">
          <el-col :span="8" v-for="tech in techs" :key="tech.name">
            <div class="tech-card">
              <i :class="tech.icon" :style="{ color: tech.color }"></i>
              <strong>{{ tech.name }}</strong>
              <span>{{ tech.desc }}</span>
            </div>
          </el-col>
        </el-row>
        <el-divider />
        <p class="desc">
          本系统为"实验一：HTML 静态网页设计"的前端原型系统。
          使用 Vue + ElementUI 构建，实现了登录、注册、忘记密码、主页、信息页面和用户列表等完整页面。
          所有数据均为前端静态模拟数据，不涉及后端接口和数据库。
        </p>
      </el-card>

      <div class="actions">
        <el-button type="primary" icon="el-icon-s-home" @click="$router.push('/home')">返回主页</el-button>
        <el-button type="success" icon="el-icon-user" @click="$router.push('/users')">用户管理</el-button>
        <el-button type="info" icon="el-icon-s-tools" @click="handleMore">更多信息</el-button>
      </div>
    </div>
  </app-layout>
</template>

<script>
import AppLayout from '../components/AppLayout.vue';

export default {
  name: 'InfoPage',
  components: { AppLayout },
  data() {
    return {
      userInfo: {
        name: '张三',
        username: 'zhangsan',
        gender: '男',
        age: 20,
        email: 'zhangsan@example.com',
        birthday: '2006-05',
        address: '湖北武汉',
        status: '在线',
        registerTime: '2026-01-15'
      },
      fields: [
        { label: '用户名', key: 'username' },
        { label: '性别', key: 'gender' },
        { label: '年龄', key: 'age', suffix: ' 岁' },
        { label: '邮箱', key: 'email' },
        { label: '出生年月', key: 'birthday' },
        { label: '地址', key: 'address' },
        { label: '注册时间', key: 'registerTime' }
      ],
      techs: [
        { name: 'Vue 2.7', desc: '前端框架', icon: 'el-icon-s-platform', color: '#409EFF' },
        { name: 'ElementUI', desc: 'UI 组件库', icon: 'el-icon-s-marketing', color: '#67C23A' },
        { name: 'vue-router', desc: '路由管理', icon: 'el-icon-s-promotion', color: '#E6A23C' }
      ]
    };
  },
  methods: {
    handleMore() {
      this.$message.info('当前为静态原型系统，更多信息将在后续实验中补充');
    }
  }
};
</script>

<style scoped>
.info-page    { max-width: 1200px; }
.section-card { border-radius: 10px; margin-bottom: 20px; }

.profile {
  display: flex;
  gap: 40px;
  align-items: flex-start;
}
.profile-side {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 12px;
  min-width: 140px;
}
.profile-side strong {
  font-size: 18px;
  color: #303133;
}
.avatar-circle {
  width: 100px;
  height: 100px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(135deg, var(--accent-gradient-start, #409EFF), var(--accent-gradient-end, #66b1ff));
}
.avatar-circle i {
  font-size: 48px;
  color: #fff;
}

.profile-grid {
  flex: 1;
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 16px;
}
.kv {
  display: flex;
  padding: 10px 14px;
  background: #f9fafc;
  border-radius: 8px;
  border-left: 3px solid var(--primary-color, #409EFF);
}
.kv-label { color: #909399; font-size: 14px; min-width: 80px; }
.kv-value { color: #303133; font-size: 14px; font-weight: 500; }

.tech-card {
  text-align: center;
  padding: 20px;
  background: #f9fafc;
  border-radius: 10px;
  transition: transform 0.2s ease;
}
.tech-card:hover { transform: translateY(-2px); }
.tech-card i      { font-size: 36px; display: block; margin-bottom: 10px; }
.tech-card strong { display: block; font-size: 18px; color: #303133; margin-bottom: 4px; }
.tech-card span   { font-size: 13px; color: #909399; }

.desc {
  font-size: 14px;
  color: #606266;
  line-height: 1.8;
  text-indent: 2em;
}
.actions {
  display: flex;
  gap: 12px;
  margin-top: 8px;
}

@media (max-width: 800px) {
  .profile       { flex-direction: column; align-items: center; }
  .profile-grid  { grid-template-columns: 1fr; }
  .actions       { flex-wrap: wrap; }
}
</style>
