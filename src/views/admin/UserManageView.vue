<template>
  <section class="page">
    <div class="page-header"><h1>用户管理</h1></div>
    <el-form :inline="true" :model="query">
      <el-form-item label="用户名"><el-input v-model="query.username" clearable /></el-form-item>
      <el-form-item label="邮箱"><el-input v-model="query.email" clearable /></el-form-item>
      <el-form-item label="角色"><el-select v-model="query.role" clearable><el-option label="普通用户" value="USER" /><el-option label="管理员" value="ADMIN" /></el-select></el-form-item>
      <el-form-item label="状态"><el-select v-model="query.status" clearable><el-option label="正常" value="NORMAL" /><el-option label="禁用" value="DISABLED" /></el-select></el-form-item>
      <el-form-item><el-button type="primary" @click="load">查询</el-button></el-form-item>
    </el-form>
    <el-table :data="rows" border stripe>
      <el-table-column prop="username" label="用户名" />
      <el-table-column prop="email" label="邮箱" />
      <el-table-column prop="role" label="角色" width="100" />
      <el-table-column prop="status" label="状态" width="100" />
      <el-table-column prop="createdTime" label="创建时间" min-width="160" />
      <el-table-column label="操作" width="110">
        <template #default="{ row }">
          <el-button v-if="row.role !== 'ADMIN'" size="small" :type="row.status === 'NORMAL' ? 'warning' : 'success'" @click="toggle(row)">
            {{ row.status === 'NORMAL' ? '禁用' : '启用' }}
          </el-button>
        </template>
      </el-table-column>
    </el-table>
    <el-pagination layout="total, prev, pager, next" :total="total" :page-size="query.pageSize" v-model:current-page="query.pageNum" @current-change="load" />
  </section>
</template>

<script setup>
import { onMounted, reactive, ref } from 'vue';
import { ElMessage } from 'element-plus';
import { listAdminUsers, updateUserStatus } from '../../api/adminUsers';

const query = reactive({ username: '', email: '', role: '', status: '', pageNum: 1, pageSize: 10 });
const rows = ref([]);
const total = ref(0);

async function load() {
  const result = await listAdminUsers(query);
  if (result.success) {
    rows.value = result.data.list;
    total.value = result.data.total;
  }
}
async function toggle(row) {
  const result = await updateUserStatus(row.id, row.status === 'NORMAL' ? 'DISABLED' : 'NORMAL');
  if (!result.success) return ElMessage.error(result.message);
  load();
}
onMounted(load);
</script>
