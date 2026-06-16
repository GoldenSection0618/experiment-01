<template>
  <section class="page login-page">
    <el-card class="login-card" shadow="never">
      <template #header>登录占位</template>
      <el-form label-position="top">
        <el-form-item label="用户名">
          <el-input v-model="form.username" placeholder="请输入用户名" />
        </el-form-item>
        <el-form-item label="密码">
          <el-input v-model="form.password" type="password" placeholder="请输入密码" show-password />
        </el-form-item>
        <el-button type="primary" @click="showPlaceholder">登录</el-button>
      </el-form>
    </el-card>
  </section>
</template>

<script setup>
import { reactive } from 'vue';
import { ElMessage } from 'element-plus';

const form = reactive({
  username: '',
  password: ''
});

function showPlaceholder() {
  ElMessage.info('登录接口将在后续 JWT 阶段接入');
}
</script>
