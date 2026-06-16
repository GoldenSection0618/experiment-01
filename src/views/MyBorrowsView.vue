<template>
  <section class="page book-page">
    <div class="page-header">
      <div>
        <h1>我的借阅</h1>
        <p>只显示当前登录用户自己的借阅记录。</p>
      </div>
      <el-button type="primary" :loading="loading" @click="loadBorrows">刷新</el-button>
    </div>

    <el-alert v-if="errorMessage" :title="errorMessage" type="error" show-icon :closable="false" />

    <el-table v-loading="loading" :data="records" border stripe class="book-table">
      <el-table-column prop="title" label="书名" min-width="160" />
      <el-table-column prop="author" label="作者" min-width="120" />
      <el-table-column prop="borrowTime" label="借阅时间" min-width="165" />
      <el-table-column prop="dueTime" label="应还时间" min-width="165" />
      <el-table-column label="时间状态" min-width="130">
        <template #default="{ row }">
          <el-tag :type="timeStatusType(row)">{{ timeStatusText(row) }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="returnTime" label="归还时间" min-width="165">
        <template #default="{ row }">{{ row.returnTime || '-' }}</template>
      </el-table-column>
      <el-table-column prop="status" label="状态" width="100">
        <template #default="{ row }">
          <el-tag :type="statusType(row.status)">{{ statusText(row.status) }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="续借次数" width="115">
        <template #default="{ row }">{{ row.renewCount || 0 }} / {{ row.maxRenewCount ?? '-' }}</template>
      </el-table-column>
      <el-table-column prop="overdueDays" label="逾期天数" width="95" />
      <el-table-column label="罚金" width="115">
        <template #default="{ row }">
          {{ money(row.fineAmount) }}
          <span v-if="row.finePerDay">/ {{ money(row.finePerDay) }}每天</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="210">
        <template #default="{ row }">
          <el-button
            v-if="canRenew(row)"
            size="small"
            :loading="actionKey === `renew-${row.id}`"
            :disabled="actionKey !== ''"
            @click="handleRenew(row)"
          >
            续借
          </el-button>
          <el-button v-else-if="row.status === 'BORROWED'" size="small" disabled>已达续借上限</el-button>
          <el-button
            v-if="row.status === 'BORROWED' || row.status === 'OVERDUE'"
            type="primary"
            size="small"
            :loading="actionKey === `return-${row.id}`"
            :disabled="actionKey !== ''"
            @click="handleReturn(row)"
          >
            归还
          </el-button>
        </template>
      </el-table-column>
    </el-table>
  </section>
</template>

<script setup>
import { onMounted, ref } from 'vue';
import { ElMessage } from 'element-plus';
import { listMyBorrows, renewBorrow, returnBorrow } from '../api/borrows';

const records = ref([]);
const loading = ref(false);
const errorMessage = ref('');
const actionKey = ref('');

async function loadBorrows() {
  loading.value = true;
  errorMessage.value = '';
  try {
    const result = await listMyBorrows();
    if (!result.success) {
      errorMessage.value = result.message || '借阅记录加载失败';
      records.value = [];
      return;
    }
    records.value = result.data || [];
  } catch (error) {
    errorMessage.value = '无法连接后端服务';
    records.value = [];
  } finally {
    loading.value = false;
  }
}

async function handleRenew(row) {
  actionKey.value = `renew-${row.id}`;
  try {
    const result = await renewBorrow(row.id);
    if (!result.success) {
      ElMessage.error(result.message || '续借失败');
      return;
    }
    ElMessage.success('续借成功');
    await loadBorrows();
  } catch (error) {
    ElMessage.error('续借失败');
  } finally {
    actionKey.value = '';
  }
}

async function handleReturn(row) {
  actionKey.value = `return-${row.id}`;
  try {
    const result = await returnBorrow(row.id);
    if (!result.success) {
      ElMessage.error(result.message || '归还失败');
      return;
    }
    ElMessage.success('归还成功');
    await loadBorrows();
  } catch (error) {
    ElMessage.error('归还失败');
  } finally {
    actionKey.value = '';
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

function canRenew(row) {
  return row.status === 'BORROWED' && (row.maxRenewCount == null || row.renewCount < row.maxRenewCount);
}

function timeStatusText(row) {
  if (row.status === 'RETURNED') return '已归还';
  if (row.status === 'OVERDUE') return `已逾期 ${row.overdueDays || 0} 天`;
  const days = remainingDays(row.dueTime);
  if (days < 0) return `已逾期 ${Math.abs(days)} 天`;
  if (days === 0) return '今天到期';
  return `剩余 ${days} 天`;
}

function timeStatusType(row) {
  if (row.status === 'OVERDUE') return 'danger';
  if (row.status === 'RETURNED') return 'info';
  return remainingDays(row.dueTime) <= 1 ? 'warning' : 'success';
}

function remainingDays(dueTime) {
  if (!dueTime) return 0;
  const due = new Date(dueTime);
  const today = new Date();
  due.setHours(0, 0, 0, 0);
  today.setHours(0, 0, 0, 0);
  return Math.ceil((due.getTime() - today.getTime()) / 86400000);
}

function money(value) {
  return Number(value || 0).toFixed(2);
}

onMounted(loadBorrows);
</script>
