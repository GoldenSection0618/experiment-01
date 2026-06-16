<template>
  <AuthLayout>
    <h2 class="form-title">用户登录</h2>
    <p class="form-subtitle">请输入账号信息</p>

    <el-form ref="formRef" :model="form" :rules="rules" label-position="top" @keyup.enter.native="handleLogin">
      <el-form-item label="用户名" prop="username">
        <el-input v-model="form.username" placeholder="请输入用户名" prefix-icon="el-icon-user" clearable />
      </el-form-item>
      <el-form-item label="密码" prop="password">
        <el-input v-model="form.password" placeholder="请输入密码" type="password" prefix-icon="el-icon-lock" show-password clearable />
      </el-form-item>
      <el-button type="primary" class="submit-btn" :loading="loading" @click="handleLogin">登录</el-button>
    </el-form>

    <div class="form-links">
      <el-button type="text" @click="$router.push('/register')">注册用户</el-button>
      <el-button type="text" @click="$router.push('/forgot-password')">忘记密码</el-button>
    </div>

    <el-divider>第三方登录</el-divider>
    <div class="third-party">
      <el-button v-for="item in thirdParties" :key="item.name" plain size="small" @click="handleThird(item.name)">
        <i :class="item.icon"></i>
        {{ item.name }}
      </el-button>
    </div>

    <template #side>
      <div class="auth-intro">
        <i class="el-icon-s-platform"></i>
        <h1>实验二</h1>
        <p>SpringBoot Web 系统</p>
        <p class="demo-account">演示账号：zhangsan / 123456</p>
      </div>
    </template>
  </AuthLayout>
</template>

<script>
import AuthLayout from '../components/AuthLayout.vue';
import { login } from '../api/auth';
import { saveUser } from '../utils/session';
import { passwordRules, usernameRules } from '../utils/validators';

export default {
  name: 'LoginPage',
  components: { AuthLayout },
  data() {
    return {
      form: { username: '', password: '' },
      rules: {
        username: usernameRules,
        password: passwordRules
      },
      loading: false,
      thirdParties: [
        { name: 'QQ', icon: 'el-icon-chat-round' },
        { name: '微信', icon: 'el-icon-chat-square' },
        { name: '支付宝', icon: 'el-icon-s-order' }
      ]
    };
  },
  methods: {
    async handleLogin() {
      this.$refs.formRef.validate(valid => {
        if (valid) this.submitLogin();
      });
    },
    async submitLogin() {
      this.loading = true;
      try {
        const { data: result } = await login(this.form);
        if (!result.success) {
          this.$message.error(result.message);
          return;
        }
        saveUser(result.data);
        this.$message.success(result.message || '登录成功');
        this.$router.push('/home');
      } catch (error) {
        this.$message.error('后端服务暂不可用');
      } finally {
        this.loading = false;
      }
    },
    handleThird(name) {
      this.$message.info(`${name}登录仅作静态展示`);
    }
  }
};
</script>

<style scoped>
.form-links {
  display: flex;
  justify-content: space-between;
  margin-top: 14px;
}

.third-party {
  display: flex;
  justify-content: center;
  gap: 10px;
  flex-wrap: wrap;
}

.demo-account {
  margin-top: 10px;
  font-size: 13px;
}
</style>
