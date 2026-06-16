<template>
  <section class="page login-page">
    <el-card class="login-card" shadow="never">
      <template #header>修改密码</template>
      <el-form ref="formRef" :model="form" :rules="rules" label-position="top">
        <el-form-item label="旧密码" prop="oldPassword">
          <el-input v-model="form.oldPassword" type="password" show-password />
        </el-form-item>
        <el-form-item label="新密码" prop="newPassword">
          <el-input v-model="form.newPassword" type="password" show-password />
        </el-form-item>
        <el-form-item label="确认新密码" prop="confirmPassword">
          <el-input v-model="form.confirmPassword" type="password" show-password />
        </el-form-item>
        <el-button type="primary" :loading="loading" @click="submit">保存</el-button>
      </el-form>
    </el-card>
  </section>
</template>

<script setup>
import { reactive, ref } from 'vue';
import { ElMessage } from 'element-plus';
import { useRouter } from 'vue-router';
import { changePassword } from '../api/auth';
import { useUserStore } from '../stores/user';

const router = useRouter();
const userStore = useUserStore();
const formRef = ref();
const loading = ref(false);

const form = reactive({
  oldPassword: '',
  newPassword: '',
  confirmPassword: ''
});

const rules = {
  oldPassword: [{ required: true, message: '请输入旧密码', trigger: 'blur' }],
  newPassword: [
    { required: true, message: '请输入新密码', trigger: 'blur' },
    { min: 6, message: '新密码长度不能少于 6 位', trigger: 'blur' }
  ],
  confirmPassword: [
    { required: true, message: '请确认新密码', trigger: 'blur' },
    {
      validator: (rule, value, callback) => {
        if (value !== form.newPassword) {
          callback(new Error('两次密码不一致'));
          return;
        }
        callback();
      },
      trigger: 'blur'
    }
  ]
};

async function submit() {
  await formRef.value.validate();
  loading.value = true;
  try {
    const result = await changePassword(form);
    if (!result.success) {
      ElMessage.error(result.message);
      return;
    }
    ElMessage.success('密码已修改，请重新登录');
    userStore.clearLoginInfo();
    router.push('/login');
  } catch (error) {
    ElMessage.error('密码修改失败');
  } finally {
    loading.value = false;
  }
}
</script>
