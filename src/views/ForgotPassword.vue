<template>
  <div class="form-page">
    <section class="form-section">
      <div class="form-card" style="max-width: 480px;">
        <h2 class="form-title">修改密码</h2>
        <p class="form-subtitle">请填写以下信息以重置您的密码</p>

        <el-form ref="formRef" :model="form" :rules="rules" label-position="top">
          <el-form-item label="用户名" prop="username">
            <el-input v-model="form.username" placeholder="请输入您的用户名" prefix-icon="el-icon-user" clearable />
          </el-form-item>
          <el-form-item label="邮箱" prop="email">
            <el-input v-model="form.email" placeholder="请输入注册时使用的邮箱" prefix-icon="el-icon-message" clearable />
          </el-form-item>
          <el-form-item label="新密码" prop="newPassword">
            <el-input v-model="form.newPassword" placeholder="请输入新密码" type="password" prefix-icon="el-icon-lock" show-password clearable />
          </el-form-item>
          <el-form-item label="确认密码" prop="confirmPassword">
            <el-input v-model="form.confirmPassword" placeholder="请再次输入新密码" type="password" prefix-icon="el-icon-lock" show-password clearable />
          </el-form-item>
          <el-form-item>
            <el-button type="primary" class="submit-btn" @click="handleConfirm" :loading="loading">确认修改</el-button>
          </el-form-item>
          <div class="form-footer">
            <el-button type="text" @click="$router.push('/login')">返回登录</el-button>
          </div>
        </el-form>
      </div>
    </section>

    <section class="info-section">
      <div class="info-content">
        <div class="info-icon"><i class="el-icon-s-marketing"></i></div>
        <h3 class="info-title">安全提示</h3>
        <div class="info-tips">
          <div class="tip-item">
            <i class="el-icon-check"></i>
            <span>密码长度至少 3 位</span>
          </div>
          <div class="tip-item">
            <i class="el-icon-check"></i>
            <span>建议使用字母、数字和符号的组合</span>
          </div>
          <div class="tip-item">
            <i class="el-icon-check"></i>
            <span>新密码与确认密码必须一致</span>
          </div>
          <div class="tip-item">
            <i class="el-icon-check"></i>
            <span>修改成功后请使用新密码登录</span>
          </div>
        </div>
      </div>
    </section>
  </div>
</template>

<script>
export default {
  name: 'ForgotPasswordPage',
  data() {
    const validateConfirm = (_r, v, cb) => {
      if (!v) {
        cb(new Error('确认密码不能为空'));
      } else if (v !== this.form.newPassword) {
        cb(new Error('两次输入的密码不一致'));
      } else {
        cb();
      }
    };
    return {
      form: {
        username: '',
        email: '',
        newPassword: '',
        confirmPassword: ''
      },
      rules: {
        username: [
          { required: true, message: '用户名不能为空', trigger: 'blur' }
        ],
        email: [
          { required: true, message: '邮箱不能为空', trigger: 'blur' },
          { type: 'email', message: '请输入正确的邮箱格式', trigger: 'blur' }
        ],
        newPassword: [
          { required: true, message: '新密码不能为空', trigger: 'blur' },
          { min: 3, max: 30, message: '密码长度 3-30 字符', trigger: 'blur' }
        ],
        confirmPassword: [
          { required: true, message: '确认密码不能为空', trigger: 'blur' },
          { validator: validateConfirm, trigger: 'blur' }
        ]
      },
      loading: false
    };
  },
  methods: {
    handleConfirm() {
      this.$refs.formRef.validate(valid => {
        if (!valid) {
          this.$message.warning('请检查表单填写是否完整');
          return;
        }
        this.loading = true;
        setTimeout(() => {
          this.loading = false;
          this.$message.success('密码修改成功！即将跳转到登录页面');
          setTimeout(() => {
            this.$router.push('/login');
          }, 1000);
        }, 800);
      });
    }
  }
};
</script>

<style scoped>
.info-tips {
  display: flex;
  flex-direction: column;
  gap: 14px;
  text-align: left;
}
.tip-item {
  display: flex;
  align-items: center;
  gap: 10px;
  font-size: 14px;
  color: #606266;
  padding: 10px 14px;
  background: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.04);
}
.tip-item i {
  color: var(--primary-color, #409EFF);
  font-size: 16px;
  flex-shrink: 0;
}
</style>
