<template>
  <div class="form-page">
    <section class="form-section">
      <div class="form-card" style="max-width:560px;">
        <h2 class="form-title">创建账户</h2>
        <p class="form-subtitle">填写以下信息完成注册</p>

        <el-form ref="formRef" :model="form" :rules="rules" label-position="top">
          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item label="用户名" prop="username">
                <el-input v-model="form.username" placeholder="请输入用户名" prefix-icon="el-icon-user" clearable />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="密码" prop="password">
                <el-input v-model="form.password" placeholder="请输入密码" type="password" prefix-icon="el-icon-lock" show-password clearable />
              </el-form-item>
            </el-col>
          </el-row>

          <el-form-item label="邮箱" prop="email">
            <el-input v-model="form.email" placeholder="请输入邮箱地址" prefix-icon="el-icon-message" clearable />
          </el-form-item>

          <el-form-item label="出生年月" prop="birthday">
            <el-date-picker v-model="form.birthday" type="month" placeholder="请选择出生年月" value-format="yyyy-MM" style="width:100%;" />
          </el-form-item>

          <el-form-item>
            <el-button type="primary" class="submit-btn" @click="handleRegister" :loading="loading">注 册</el-button>
          </el-form-item>

          <div class="form-footer">
            <span>已有账户？</span>
            <el-button type="text" @click="$router.push('/login')">返回登录</el-button>
          </div>
        </el-form>
      </div>
    </section>

    <AvatarUploader />
  </div>
</template>

<script>
import AvatarUploader from '../components/AvatarUploader.vue';

const validateEmail = (_r, v, cb) => {
  if (!v) return cb(new Error('邮箱不能为空'));
  if (!/^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/.test(v)) return cb(new Error('请输入正确的邮箱格式'));
  cb();
};

export default {
  name: 'RegisterPage',
  components: { AvatarUploader },
  data() {
    return {
      form: { username: '', password: '', email: '', birthday: '' },
      rules: {
        username: [{ required: true, message: '用户名不能为空', trigger: 'blur' }, { min: 2, max: 20, message: '用户名长度 2-20 字符', trigger: 'blur' }],
        password: [{ required: true, message: '密码不能为空', trigger: 'blur' }, { min: 3, max: 30, message: '密码长度 3-30 字符', trigger: 'blur' }],
        email: [{ required: true, message: '邮箱不能为空', trigger: 'blur' }, { validator: validateEmail, trigger: 'blur' }],
        birthday: [{ required: true, message: '出生年月必须填写', trigger: 'change' }]
      },
      loading: false
    };
  },
  methods: {
    handleRegister() {
      this.$refs.formRef.validate(valid => {
        if (!valid) return this.$message.warning('请检查表单填写是否完整');
        this.loading = true;
        setTimeout(() => {
          this.loading = false;
          this.$message.success('注册成功！即将跳转到登录页面');
          setTimeout(() => this.$router.push('/login'), 1000);
        }, 800);
      });
    }
  }
};
</script>
