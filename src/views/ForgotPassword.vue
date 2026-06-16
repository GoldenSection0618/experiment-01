<template>
  <AuthLayout>
    <h2 class="form-title">修改密码</h2>
    <p class="form-subtitle">请填写账号和新密码</p>

    <el-form ref="formRef" :model="form" :rules="rules" label-position="top">
      <el-row :gutter="16">
        <el-col :xs="24" :sm="24" :md="12">
          <el-form-item label="用户名" prop="username">
            <el-input v-model="form.username" placeholder="请输入用户名" prefix-icon="el-icon-user" clearable />
          </el-form-item>
        </el-col>
        <el-col :xs="24" :sm="24" :md="12">
          <el-form-item label="邮箱" prop="email">
            <el-input v-model="form.email" placeholder="请输入邮箱" prefix-icon="el-icon-message" clearable />
          </el-form-item>
        </el-col>
      </el-row>

      <el-form-item label="新密码" prop="newPassword">
        <el-input v-model="form.newPassword" placeholder="请输入新密码" type="password" prefix-icon="el-icon-lock" show-password clearable />
      </el-form-item>
      <el-form-item label="确认密码" prop="confirmPassword">
        <el-input v-model="form.confirmPassword" placeholder="请再次输入新密码" type="password" prefix-icon="el-icon-lock" show-password clearable />
      </el-form-item>

      <el-button type="primary" class="submit-btn" :loading="loading" @click="handleConfirm">确认修改</el-button>
      <div class="form-footer">
        <el-button type="text" @click="$router.push('/login')">返回登录</el-button>
      </div>
    </el-form>

    <template #side>
      <div class="auth-tip">
        <i class="el-icon-lock"></i>
        <h3>安全提示</h3>
        <p>确认密码需要和新密码保持一致。</p>
      </div>
    </template>
  </AuthLayout>
</template>

<script>
import AuthLayout from '../components/AuthLayout.vue';
import { confirmPasswordRules, emailRules, passwordRules, usernameRules } from '../utils/validators';

export default {
  name: 'ForgotPasswordPage',
  components: { AuthLayout },
  data() {
    return {
      form: { username: '', email: '', newPassword: '', confirmPassword: '' },
      rules: {
        username: usernameRules,
        email: emailRules,
        newPassword: passwordRules,
        confirmPassword: confirmPasswordRules(() => this.form.newPassword)
      },
      loading: false
    };
  },
  methods: {
    handleConfirm() {
      this.$refs.formRef.validate(valid => {
        if (!valid) return;
        this.loading = true;
        setTimeout(() => {
          this.loading = false;
          this.$message.success('密码修改成功');
          this.$router.push('/login');
        }, 400);
      });
    }
  }
};
</script>
