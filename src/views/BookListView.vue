<template>
  <section class="page book-page">
    <div class="page-header">
      <div>
        <h1>图书列表</h1>
        <p>数据来自后端 /api/books 接口和 MyBatis XML 查询。</p>
      </div>
      <el-button type="primary" :loading="loading" @click="loadBooks">刷新</el-button>
    </div>

    <el-alert v-if="errorMessage" :title="errorMessage" type="error" show-icon :closable="false" />

    <el-table v-loading="loading" :data="books" border stripe class="book-table">
      <el-table-column prop="title" label="书名" min-width="160" />
      <el-table-column prop="categoryName" label="分类" width="110" />
      <el-table-column prop="author" label="作者" min-width="140" />
      <el-table-column prop="publisher" label="出版社" min-width="150" />
      <el-table-column prop="isbn" label="ISBN" min-width="150" />
      <el-table-column prop="totalStock" label="总库存" width="90" />
      <el-table-column prop="availableStock" label="可借库存" width="100" />
      <el-table-column prop="status" label="状态" width="110">
        <template #default="{ row }">
          <el-tag :type="row.status === 'ON_SHELF' ? 'success' : 'info'">
            {{ row.status === 'ON_SHELF' ? '上架' : '下架' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="110">
        <template #default="{ row }">
          <el-button
            v-if="userStore.role === 'USER'"
            type="primary"
            size="small"
            :disabled="row.status !== 'ON_SHELF' || row.availableStock <= 0"
            @click="handleBorrow(row)"
          >
            借阅
          </el-button>
        </template>
      </el-table-column>
    </el-table>
  </section>
</template>

<script setup>
import { onMounted, ref } from 'vue';
import { ElMessage } from 'element-plus';
import { listBooks } from '../api/books';
import { borrowBook } from '../api/borrows';
import { useUserStore } from '../stores/user';

const books = ref([]);
const loading = ref(false);
const errorMessage = ref('');
const userStore = useUserStore();

async function loadBooks() {
  loading.value = true;
  errorMessage.value = '';
  try {
    const result = await listBooks();
    if (!result.success) {
      errorMessage.value = result.message || '图书列表加载失败';
      books.value = [];
      return;
    }
    books.value = result.data || [];
  } catch (error) {
    errorMessage.value = '无法连接后端服务';
    books.value = [];
  } finally {
    loading.value = false;
  }
}

async function handleBorrow(row) {
  try {
    const result = await borrowBook(row.id);
    if (!result.success) {
      ElMessage.error(result.message);
      return;
    }
    ElMessage.success('借阅成功');
    loadBooks();
  } catch (error) {
    ElMessage.error('借阅失败');
  }
}

onMounted(loadBooks);
</script>
