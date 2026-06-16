<template>
  <div class="form-page">
    <section class="brand-section">
      <div class="brand-content">
        <div class="brand-icon"><i class="el-icon-s-platform"></i></div>
        <h1 class="brand-title">实验一 · 静态网页设计</h1>
        <p class="brand-desc">Vue + ElementUI 响应式前端系统原型</p>
        <div class="brand-features">
          <div class="feature-item">
            <i class="el-icon-check"></i>
            <span>响应式布局</span>
          </div>
          <div class="feature-item">
            <i class="el-icon-check"></i>
            <span>主题切换</span>
          </div>
          <div class="feature-item">
            <i class="el-icon-check"></i>
            <span>表单验证</span>
          </div>
        </div>
      </div>
    </section>

    <section class="form-section">
      <div class="form-card" style="max-width: 420px;">
        <h2 class="form-title">用户登录</h2>
        <p class="form-subtitle">欢迎回来，请登录您的账户</p>

        <el-form ref="formRef" :model="form" :rules="rules" label-position="top" @keyup.enter.native="handleLogin">
          <el-form-item label="用户名" prop="username">
            <el-input v-model="form.username" placeholder="请输入用户名" prefix-icon="el-icon-user" clearable />
          </el-form-item>
          <el-form-item label="密码" prop="password">
            <el-input v-model="form.password" placeholder="请输入密码" type="password" prefix-icon="el-icon-lock" show-password clearable />
          </el-form-item>
          <el-form-item>
            <el-button type="primary" class="submit-btn" @click="handleLogin" :loading="loading">登 录</el-button>
          </el-form-item>
        </el-form>

        <div class="form-links">
          <el-button type="text" @click="$router.push('/register')">注册用户</el-button>
          <el-button type="text" @click="$router.push('/forgot-password')">忘记密码</el-button>
        </div>

        <el-divider>第三方登录</el-divider>

        <div class="third-party">
          <div class="tp-btn" v-for="item in thirdParties" :key="item.name" @click="handleThird(item.name)">
            <i :class="item.icon" :style="{ color: item.color }"></i>
            <span>{{ item.name }}登录</span>
          </div>
        </div>
      </div>
    </section>
  </div>
</template>

<script>
export default {
  name: 'LoginPage',
  data() {
    return {
      form: {
        username: '',
        password: ''
      },
      rules: {
        username: [
          { required: true, message: '用户名不能为空', trigger: 'blur' },
          { min: 2, max: 20, message: '用户名长度 2-20 字符', trigger: 'blur' }
        ],
        password: [
          { required: true, message: '密码不能为空', trigger: 'blur' },
          { min: 3, max: 30, message: '密码长度 3-30 字符', trigger: 'blur' }
        ]
      },
      loading: false,
      thirdParties: [
        { name: 'QQ', icon: 'el-icon-chat-round', color: '#12B7F5' },
        { name: '微信', icon: 'el-icon-chat-square', color: '#07C160' },
        { name: '支付宝', icon: 'el-icon-s-order', color: '#1677FF' }
      ]
    };
  },
  methods: {
    handleLogin() {
      this.$refs.formRef.validate(valid => {
        if (!valid) {
          this.$message.warning('请检查表单填写是否完整');
          return;
        }
        this.loading = true;
        setTimeout(() => {
          this.loading = false;
          this.$message.success('登录成功！');
          this.$router.push('/home');
        }, 800);
      });
    },
    handleThird(platform) {
      this.$message.info(`静态原型暂未接入${platform}登录`);
    }
  }
};
</script>

<style scoped>
.form-links {
  display: flex;
  justify-content: space-between;
  margin-bottom: 8px;
}

.brand-features {
  display: flex;
  flex-direction: column;
  gap: 12px;
  align-items: center;
}
.feature-item {
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 15px;
  opacity: 0.9;
}
.feature-item i { font-size: 18px; }

.third-party {
  display: flex;
  justify-content: center;
  gap: 20px;
  margin-top: 8px;
}
.tp-btn {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 6px;
  cursor: pointer;
  padding: 12px 16px;
  border-radius: 8px;
  border: 1px solid #ebeef5;
  min-width: 80px;
  transition: all 0.25s ease;
}
.tp-btn:hover {
  background: #f5f7fa;
  border-color: #d3d9e6;
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.08);
}
.tp-btn i    { font-size: 24px; }
.tp-btn span { font-size: 12px; color: #606266; }
</style>
