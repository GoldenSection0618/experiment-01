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
      <el-table-column label="欠费金额" width="120">
        <template #default="{ row }">
          <el-button v-if="hasDebt(row)" link type="danger" @click="handleDebt(row)">
            {{ money(row.debtAmount) }}
          </el-button>
          <span v-else>{{ money(row.debtAmount) }}</span>
        </template>
      </el-table-column>
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
import { ElMessage, ElMessageBox } from 'element-plus';
import { clearUserDebt, getUserDebt, listAdminUsers, updateUserStatus } from '../../api/adminUsers';

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

async function handleDebt(row) {
  const result = await getUserDebt(row.id);
  if (!result.success) {
    ElMessage.error(result.message || '欠费信息查询失败');
    return;
  }
  const debt = result.data;
  if (debt.hasUnreturnedOverdue) {
    await ElMessageBox.alert(`该用户还有逾期图书未归还：${debt.unreturnedBooks.join('、')}`, '不能清除欠费', {
      confirmButtonText: '知道了',
      type: 'warning'
    });
    await load();
    return;
  }
  try {
    await ElMessageBox.confirm(`确认帮助用户 ${row.username} 清除 ${money(debt.debtAmount)} 元欠费？`, '清除欠费', {
      confirmButtonText: '确认清除',
      cancelButtonText: '取消',
      type: 'warning'
    });
  } catch (error) {
    return;
  }
  const clearResult = await clearUserDebt(row.id);
  if (!clearResult.success) {
    ElMessage.error(clearResult.message || '欠费清除失败');
    return;
  }
  ElMessage.success(clearResult.message || '欠费已清除');
  await load();
}

function hasDebt(row) {
  return Number(row.debtAmount || 0) > 0;
}

function money(value) {
  return Number(value || 0).toFixed(2);
}

onMounted(load);
</script>
