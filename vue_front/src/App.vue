<template>
  <el-container class="app-shell">
    <el-header class="app-header">
      <div class="brand">校园图书借阅自助管理系统</div>
      <el-menu :default-active="activePath" mode="horizontal" router class="nav-menu">
        <template v-if="userStore.isLoggedIn">
          <el-menu-item index="/home">首页</el-menu-item>
          <el-menu-item index="/books">图书列表</el-menu-item>
          <el-menu-item v-if="userStore.role === 'USER'" index="/my-borrows">我的借阅</el-menu-item>
          <template v-if="userStore.role === 'ADMIN'">
            <el-menu-item index="/admin">管理首页</el-menu-item>
            <el-menu-item index="/admin/categories">分类管理</el-menu-item>
            <el-menu-item index="/admin/books">图书管理</el-menu-item>
            <el-menu-item index="/admin/users">用户管理</el-menu-item>
            <el-menu-item index="/admin/borrows">借阅记录</el-menu-item>
            <el-menu-item index="/admin/statistics">统计分析</el-menu-item>
          </template>
          <el-menu-item index="/password">修改密码</el-menu-item>
        </template>
        <template v-else>
          <el-menu-item index="/login">登录</el-menu-item>
          <el-menu-item index="/register">注册</el-menu-item>
        </template>
      </el-menu>
      <div v-if="userStore.isLoggedIn" class="user-actions">
        <el-button v-if="userStore.role === 'USER'" type="success" plain @click="openAiDialog">询问 AI</el-button>
        <span class="welcome-text">欢迎 {{ displayName }}</span>
        <el-button type="primary" plain @click="logout">退出登录</el-button>
      </div>
    </el-header>

    <el-main class="app-main">
      <router-view />
    </el-main>

    <el-dialog v-model="aiDialogVisible" title="询问 AI" width="560px">
      <el-form label-position="top">
        <el-form-item label="选择图书">
          <el-select
            v-model="selectedBookId"
            filterable
            placeholder="请选择一本已上架图书"
            :loading="bookLoading"
            style="width: 100%"
          >
            <el-option
              v-for="book in aiBooks"
              :key="book.id"
              :label="`${book.title} - ${book.author}`"
              :value="book.id"
            />
          </el-select>
        </el-form-item>
        <el-button type="primary" :loading="aiLoading" @click="askAi">
          {{ aiLoading ? '大模型正在推理中' : '发送请求' }}
        </el-button>
      </el-form>
      <div v-if="aiAnswer" class="ai-answer">{{ aiAnswer }}</div>
      <p class="ai-testing-tip">该功能正在测试中</p>
    </el-dialog>
  </el-container>
</template>

<script setup>
import { computed, ref } from 'vue';
import { ElMessage } from 'element-plus';
import { useRoute, useRouter } from 'vue-router';
import { askBookIntro } from './api/ai';
import { listBooks } from './api/books';
import { useUserStore } from './stores/user';

const route = useRoute();
const router = useRouter();
const userStore = useUserStore();
userStore.loadFromStorage();
const activePath = computed(() => route.path);
const displayName = computed(() => userStore.userInfo?.username || '用户');
const aiDialogVisible = ref(false);
const aiBooks = ref([]);
const selectedBookId = ref();
const bookLoading = ref(false);
const aiLoading = ref(false);
const aiAnswer = ref('');

function logout() {
  userStore.clearLoginInfo();
  router.push('/login');
}

async function openAiDialog() {
  aiDialogVisible.value = true;
  aiAnswer.value = '';
  if (aiBooks.value.length > 0) {
    return;
  }
  bookLoading.value = true;
  try {
    const result = await listBooks({ pageNum: 1, pageSize: 100 });
    if (!result.success) {
      ElMessage.error(result.message || '图书加载失败');
      return;
    }
    aiBooks.value = result.data.list || [];
  } catch (error) {
    ElMessage.error('图书加载失败');
  } finally {
    bookLoading.value = false;
  }
}

async function askAi() {
  if (!selectedBookId.value) {
    ElMessage.warning('请先选择图书');
    return;
  }
  aiLoading.value = true;
  aiAnswer.value = '';
  try {
    const result = await askBookIntro(selectedBookId.value);
    if (!result.success) {
      ElMessage.error(result.message || 'AI 请求失败');
      return;
    }
    aiAnswer.value = result.data.answer;
  } catch (error) {
    ElMessage.error('AI 请求失败');
  } finally {
    aiLoading.value = false;
  }
}
</script>
