<template>
  <section class="page book-page">
    <div class="page-header">
      <div>
        <h1>图书列表</h1>
        <p>支持按书名、作者和分类在线查询上架图书。</p>
      </div>
      <el-button type="primary" :loading="loading" @click="loadBooks">刷新</el-button>
    </div>

    <el-alert v-if="errorMessage" :title="errorMessage" type="error" show-icon :closable="false" />

    <el-form :inline="true" :model="query" class="query-form">
      <el-form-item label="书名">
        <el-input v-model="query.title" clearable placeholder="输入书名" />
      </el-form-item>
      <el-form-item label="作者">
        <el-input v-model="query.author" clearable placeholder="输入作者" />
      </el-form-item>
      <el-form-item label="分类">
        <el-select v-model="query.categoryId" clearable placeholder="全部分类" style="width: 150px">
          <el-option v-for="category in categories" :key="category.id" :label="category.name" :value="category.id" />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" :loading="loading" @click="handleSearch">查询</el-button>
        <el-button @click="resetSearch">重置</el-button>
      </el-form-item>
    </el-form>

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
            :loading="borrowLoadingId === row.id"
            :disabled="borrowLoadingId !== null || row.status !== 'ON_SHELF' || row.availableStock <= 0"
            @click="handleBorrow(row)"
          >
            借阅
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-pagination
      class="table-pagination"
      layout="total, prev, pager, next"
      :total="total"
      :page-size="query.pageSize"
      v-model:current-page="query.pageNum"
      @current-change="loadBooks"
    />
  </section>
</template>

<script setup>
import { onMounted, reactive, ref } from 'vue';
import { ElMessage } from 'element-plus';
import { listBookCategories, listBooks } from '../api/books';
import { borrowBook } from '../api/borrows';
import { useUserStore } from '../stores/user';

const books = ref([]);
const categories = ref([]);
const total = ref(0);
const loading = ref(false);
const errorMessage = ref('');
const borrowLoadingId = ref(null);
const userStore = useUserStore();
const query = reactive({
  title: '',
  author: '',
  categoryId: null,
  pageNum: 1,
  pageSize: 10
});

async function loadBooks() {
  loading.value = true;
  errorMessage.value = '';
  try {
    const result = await listBooks(query);
    if (!result.success) {
      errorMessage.value = result.message || '图书列表加载失败';
      books.value = [];
      total.value = 0;
      return;
    }
    books.value = result.data?.list || [];
    total.value = result.data?.total || 0;
  } catch (error) {
    errorMessage.value = '无法连接后端服务';
    books.value = [];
    total.value = 0;
  } finally {
    loading.value = false;
  }
}

async function loadCategories() {
  const result = await listBookCategories();
  if (result.success) {
    categories.value = result.data || [];
  }
}

function handleSearch() {
  query.pageNum = 1;
  loadBooks();
}

function resetSearch() {
  query.title = '';
  query.author = '';
  query.categoryId = null;
  query.pageNum = 1;
  loadBooks();
}

async function handleBorrow(row) {
  borrowLoadingId.value = row.id;
  try {
    const result = await borrowBook(row.id);
    if (!result.success) {
      ElMessage.error(result.message || '借阅失败');
      return;
    }
    ElMessage.success('借阅成功');
    await loadBooks();
  } catch (error) {
    ElMessage.error('借阅失败');
  } finally {
    borrowLoadingId.value = null;
  }
}

onMounted(() => {
  loadCategories();
  loadBooks();
});
</script>
