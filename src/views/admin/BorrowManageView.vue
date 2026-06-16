<template>
  <section class="page">
    <div class="page-header"><h1>借阅记录</h1></div>
    <el-form :inline="true" :model="query">
      <el-form-item label="用户"><el-input v-model="query.username" clearable /></el-form-item>
      <el-form-item label="图书"><el-input v-model="query.title" clearable /></el-form-item>
      <el-form-item label="状态"><el-select v-model="query.status" clearable><el-option label="借阅中" value="BORROWED" /><el-option label="逾期" value="OVERDUE" /><el-option label="已归还" value="RETURNED" /></el-select></el-form-item>
      <el-form-item><el-button type="primary" @click="load">查询</el-button></el-form-item>
    </el-form>
    <el-table :data="rows" border stripe>
      <el-table-column prop="username" label="用户" width="110" />
      <el-table-column prop="categoryName" label="分类" width="100" />
      <el-table-column prop="title" label="书名" min-width="150" />
      <el-table-column prop="author" label="作者" />
      <el-table-column prop="borrowTime" label="借阅时间" min-width="160" />
      <el-table-column prop="dueTime" label="应还时间" min-width="160" />
      <el-table-column prop="returnTime" label="归还时间" min-width="160">
        <template #default="{ row }">{{ row.returnTime || '-' }}</template>
      </el-table-column>
      <el-table-column prop="status" label="状态" width="95" />
      <el-table-column prop="renewCount" label="续借" width="70" />
      <el-table-column prop="overdueDays" label="逾期天数" width="95" />
      <el-table-column prop="fineAmount" label="罚金" width="80" />
    </el-table>
    <el-pagination layout="total, prev, pager, next" :total="total" :page-size="query.pageSize" v-model:current-page="query.pageNum" @current-change="load" />
  </section>
</template>

<script setup>
import { onMounted, reactive, ref } from 'vue';
import { listAdminBorrows } from '../../api/adminBorrows';

const query = reactive({ username: '', title: '', status: '', pageNum: 1, pageSize: 10 });
const rows = ref([]);
const total = ref(0);

async function load() {
  const result = await listAdminBorrows(query);
  if (result.success) {
    rows.value = result.data.list;
    total.value = result.data.total;
  }
}
onMounted(load);
</script>
