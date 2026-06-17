<template>
  <section class="page login-page">
    <el-card class="login-card" shadow="never">
      <template #header>用户登录</template>
      <el-form ref="formRef" :model="form" :rules="rules" label-position="top">
        <el-form-item label="用户名" prop="username">
          <el-input v-model="form.username" placeholder="请输入用户名" />
        </el-form-item>
        <el-form-item label="密码" prop="password">
          <el-input v-model="form.password" type="password" placeholder="请输入密码" show-password />
        </el-form-item>
        <el-button type="primary" :loading="loading" @click="submit">登录</el-button>
        <el-button text @click="router.push('/register')">注册账号</el-button>
      </el-form>
    </el-card>
  </section>
</template>

<script setup>
import { reactive, ref } from 'vue';
import { ElMessage } from 'element-plus';
import { useRoute, useRouter } from 'vue-router';
import { login } from '../api/auth';
import { useUserStore } from '../stores/user';

const router = useRouter();
const route = useRoute();
const userStore = useUserStore();
const formRef = ref();
const loading = ref(false);

const form = reactive({
  username: '',
  password: ''
});

const rules = {
  username: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
  password: [{ required: true, message: '请输入密码', trigger: 'blur' }]
};

async function submit() {
  await formRef.value.validate();
  loading.value = true;
  try {
    const result = await login(form);
    if (!result.success) {
      ElMessage.error(result.message);
      return;
    }
    userStore.setLoginInfo(result.data);
    ElMessage.success('登录成功');
    router.push(route.query.redirect || '/home');
  } catch (error) {
    ElMessage.error('登录失败');
  } finally {
    loading.value = false;
  }
}
</script>
