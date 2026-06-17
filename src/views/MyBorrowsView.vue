<template>
  <section class="page book-page">
    <div class="page-header">
      <div>
        <h1>我的借阅</h1>
        <p>只显示当前登录用户自己的借阅记录。</p>
      </div>
      <div class="page-actions">
        <el-button @click="showFineRule">罚金规则</el-button>
        <el-button type="primary" :loading="loading" @click="loadBorrows">刷新</el-button>
      </div>
    </div>

    <el-alert v-if="errorMessage" :title="errorMessage" type="error" show-icon :closable="false" />

    <div class="borrow-section">
      <div class="borrow-section-header">
        <h2>当前借阅</h2>
        <span>{{ currentBorrows.length }} 条</span>
      </div>
      <el-table v-loading="loading" :data="currentBorrows" border stripe class="book-table">
        <el-table-column prop="title" label="书名" min-width="160" />
        <el-table-column prop="author" label="作者" min-width="120" />
        <el-table-column label="借阅时间" min-width="120">
          <template #default="{ row }">{{ formatDate(row.borrowTime) }}</template>
        </el-table-column>
        <el-table-column label="应还时间" min-width="120">
          <template #default="{ row }">{{ formatDate(row.dueTime) }}</template>
        </el-table-column>
        <el-table-column label="时间状态" min-width="130">
          <template #default="{ row }">
            <el-tag :type="timeStatusType(row)">{{ timeStatusText(row) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="续借次数" width="115">
          <template #default="{ row }">{{ row.renewCount || 0 }} / {{ row.maxRenewCount ?? '-' }}</template>
        </el-table-column>
        <el-table-column prop="overdueDays" label="逾期天数" width="95" />
        <el-table-column label="罚金" width="115">
          <template #default="{ row }">
            {{ money(row.fineAmount) }}
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
    </div>

    <div class="borrow-section">
      <div class="borrow-section-header">
        <h2>借阅记录</h2>
        <span>最多显示 8 条</span>
      </div>
      <el-table v-loading="loading" :data="historyBorrows" border stripe class="book-table">
        <el-table-column prop="title" label="书名" min-width="160" />
        <el-table-column prop="author" label="作者" min-width="120" />
        <el-table-column label="借阅时间" min-width="120">
          <template #default="{ row }">{{ formatDate(row.borrowTime) }}</template>
        </el-table-column>
        <el-table-column label="应还时间" min-width="120">
          <template #default="{ row }">{{ formatDate(row.dueTime) }}</template>
        </el-table-column>
        <el-table-column label="归还时间" min-width="120">
          <template #default="{ row }">{{ formatDate(row.returnTime) }}</template>
        </el-table-column>
        <el-table-column prop="overdueDays" label="逾期天数" width="95" />
        <el-table-column label="待缴纳罚金" width="120">
          <template #default="{ row }">
            <el-button v-if="hasFine(row)" type="primary" link @click="handlePayFine(row)">
              {{ money(row.fineAmount) }}
            </el-button>
            <span v-else>{{ money(row.fineAmount) }}</span>
          </template>
        </el-table-column>
      </el-table>
    </div>
  </section>
</template>

<script setup>
import { computed, onMounted, ref } from 'vue';
import { ElMessage, ElMessageBox } from 'element-plus';
import { listMyBorrows, renewBorrow, returnBorrow } from '../api/borrows';

const records = ref([]);
const loading = ref(false);
const errorMessage = ref('');
const actionKey = ref('');
const currentBorrows = computed(() => records.value.filter((record) => record.status !== 'RETURNED'));
const historyBorrows = computed(() => records.value
  .filter((record) => record.status === 'RETURNED')
  .sort((first, second) => Number(second.fineAmount || 0) - Number(first.fineAmount || 0))
  .slice(0, 8));

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

async function handlePayFine(row) {
  try {
    await ElMessageBox.confirm(`确认支付 ${money(row.fineAmount)} 元罚金？`, '支付确认', {
      confirmButtonText: '是',
      cancelButtonText: '否',
      type: 'warning'
    });
    ElMessage.info('该功能未开放');
  } catch (error) {
    // 用户取消时关闭弹窗即可。
  }
}

function showFineRule() {
  const finePerDay = records.value.find((record) => record.finePerDay != null)?.finePerDay;
  const text = finePerDay == null
    ? '逾期罚金按借阅规则计算，具体金额以归还时后端计算结果为准。'
    : `逾期后按每天 ${money(finePerDay)} 元计算罚金，具体金额以归还时后端计算结果为准。`;
  ElMessageBox.alert(text, '罚金规则', {
    confirmButtonText: '知道了'
  });
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

function formatDate(value) {
  if (!value) return '-';
  return String(value).slice(0, 10);
}

function hasFine(row) {
  return Number(row.fineAmount || 0) > 0;
}

function money(value) {
  return Number(value || 0).toFixed(2);
}

onMounted(loadBorrows);
</script>

<style scoped>
.borrow-section {
  margin-top: 22px;
}

.page-actions {
  display: flex;
  gap: 10px;
  flex-wrap: wrap;
  justify-content: flex-end;
}

.borrow-section-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 16px;
}

.borrow-section-header h2 {
  margin: 0;
  font-size: 18px;
}

.borrow-section-header span {
  color: #5f6b7a;
  font-size: 14px;
}
</style>
