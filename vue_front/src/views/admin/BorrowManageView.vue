<template>
  <section class="page">
    <div class="page-header"><h1>借阅记录</h1></div>
    <el-form :inline="true" :model="query">
      <el-form-item label="用户"><el-input v-model="query.username" clearable /></el-form-item>
      <el-form-item label="图书"><el-input v-model="query.title" clearable /></el-form-item>
      <el-form-item label="状态"><el-select v-model="query.status" clearable><el-option label="借阅中" value="BORROWED" /><el-option label="逾期" value="OVERDUE" /><el-option label="已归还" value="RETURNED" /></el-select></el-form-item>
      <el-form-item><el-button type="primary" :loading="loading" @click="load">查询</el-button></el-form-item>
    </el-form>
    <el-table v-loading="loading" :data="rows" border stripe>
      <el-table-column prop="username" label="用户" width="110" />
      <el-table-column prop="categoryName" label="分类" width="100" />
      <el-table-column prop="title" label="书名" min-width="150" />
      <el-table-column prop="author" label="作者" />
      <el-table-column prop="borrowTime" label="借阅时间" min-width="160" />
      <el-table-column prop="dueTime" label="应还时间" min-width="160" />
      <el-table-column prop="returnTime" label="归还时间" min-width="160">
        <template #default="{ row }">{{ row.returnTime || '-' }}</template>
      </el-table-column>
      <el-table-column prop="status" label="状态" width="95">
        <template #default="{ row }">
          <el-tag :type="statusType(row.status)">{{ statusText(row.status) }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="续借" width="95">
        <template #default="{ row }">{{ row.renewCount || 0 }} / {{ row.maxRenewCount ?? '-' }}</template>
      </el-table-column>
      <el-table-column prop="overdueDays" label="逾期天数" width="95" />
      <el-table-column label="罚金" width="115">
        <template #default="{ row }">{{ money(row.fineAmount) }}</template>
      </el-table-column>
    </el-table>
    <el-pagination layout="total, prev, pager, next" :total="total" :page-size="query.pageSize" v-model:current-page="query.pageNum" @current-change="load" />
  </section>
</template>

<script setup>
import { onMounted, reactive, ref } from 'vue';
import { ElMessage } from 'element-plus';
import { listAdminBorrows } from '../../api/adminBorrows';

const query = reactive({ username: '', title: '', status: '', pageNum: 1, pageSize: 10 });
const rows = ref([]);
const total = ref(0);
const loading = ref(false);

async function load() {
  loading.value = true;
  try {
    const result = await listAdminBorrows(query);
    if (!result.success) {
      ElMessage.error(result.message || '借阅记录加载失败');
      return;
    }
    rows.value = result.data.list || [];
    total.value = result.data.total || 0;
  } catch (error) {
    ElMessage.error('借阅记录加载失败');
  } finally {
    loading.value = false;
  }
}

function statusText(status) {
  if (status === 'BORROWED') return '借阅中';
  if (status === 'OVERDUE') return '已逾期';
  if (status === 'RETURNED') return '已归还';
  return status;
}

function statusType(status) {
  if (status === 'BORROWED') return 'success';
  if (status === 'OVERDUE') return 'danger';
  return 'info';
}

function money(value) {
  return Number(value || 0).toFixed(2);
}

onMounted(load);
</script>
