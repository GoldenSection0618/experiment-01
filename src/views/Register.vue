<template>
  <AuthLayout>
    <h2 class="form-title">用户注册</h2>
    <p class="form-subtitle">填写基本信息即可</p>

    <el-form ref="formRef" :model="form" :rules="rules" label-position="top">
      <el-row :gutter="16">
        <el-col :xs="24" :sm="24" :md="12">
          <el-form-item label="用户名" prop="username">
            <el-input v-model="form.username" placeholder="请输入用户名" prefix-icon="el-icon-user" clearable />
          </el-form-item>
        </el-col>
        <el-col :xs="24" :sm="24" :md="12">
          <el-form-item label="密码" prop="password">
            <el-input v-model="form.password" placeholder="请输入密码" type="password" prefix-icon="el-icon-lock" show-password clearable />
          </el-form-item>
        </el-col>
      </el-row>

      <el-form-item label="邮箱" prop="email">
        <el-input v-model="form.email" placeholder="请输入邮箱" prefix-icon="el-icon-message" clearable />
      </el-form-item>
      <el-form-item label="出生年月" prop="birthday">
        <el-date-picker v-model="form.birthday" type="month" value-format="yyyy-MM" placeholder="请选择出生年月" />
      </el-form-item>

      <el-button type="primary" class="submit-btn" :loading="loading" @click="handleRegister">注册</el-button>
      <div class="form-footer">
        <span>已有账户？</span>
        <el-button type="text" @click="$router.push('/login')">返回登录</el-button>
      </div>
    </el-form>

    <template #side>
      <AvatarUploader />
    </template>
  </AuthLayout>
</template>

<script>
import AuthLayout from '../components/AuthLayout.vue';
import AvatarUploader from '../components/AvatarUploader.vue';
import { birthdayRules, emailRules, passwordRules, usernameRules } from '../utils/validators';

export default {
  name: 'RegisterPage',
  components: { AuthLayout, AvatarUploader },
  data() {
    return {
      form: { username: '', password: '', email: '', birthday: '' },
      rules: {
        username: usernameRules,
        password: passwordRules,
        email: emailRules,
        birthday: birthdayRules
      },
      loading: false
    };
  },
  methods: {
    handleRegister() {
      this.$refs.formRef.validate(valid => {
        if (!valid) return;
        this.loading = true;
        setTimeout(() => {
          this.loading = false;
          this.$message.success('注册成功');
          this.$router.push('/login');
        }, 400);
      });
    }
  }
};
</script>
