<template>
  <section class="page">
    <div class="page-header">
      <div>
        <h1>统计分析</h1>
        <p>基于借阅记录、图书、分类和用户数据的多表统计。</p>
      </div>
      <el-button type="primary" :loading="loading" @click="load">刷新</el-button>
    </div>

    <el-row :gutter="16" class="stat-row">
      <el-col :xs="12" :sm="6">
        <div class="stat-box">
          <span>图书总数</span>
          <strong>{{ overview.summary?.totalBooks || 0 }}</strong>
        </div>
      </el-col>
      <el-col :xs="12" :sm="6">
        <div class="stat-box">
          <span>当前借出</span>
          <strong>{{ overview.summary?.borrowedCount || 0 }}</strong>
        </div>
      </el-col>
      <el-col :xs="12" :sm="6">
        <div class="stat-box">
          <span>逾期未还</span>
          <strong>{{ overview.summary?.overdueCount || 0 }}</strong>
        </div>
      </el-col>
      <el-col :xs="12" :sm="6">
        <div class="stat-box">
          <span>普通用户</span>
          <strong>{{ overview.summary?.userCount || 0 }}</strong>
        </div>
      </el-col>
    </el-row>

    <div class="stat-grid">
      <section>
        <h2>热门借阅图书 Top 5</h2>
        <el-table v-loading="loading" :data="overview.hotBooks" border stripe>
          <el-table-column prop="title" label="书名" min-width="150" />
          <el-table-column prop="author" label="作者" width="130" />
          <el-table-column prop="categoryName" label="分类" width="110" />
          <el-table-column prop="borrowCount" label="借阅次数" width="100" />
        </el-table>
      </section>

      <section>
        <h2>分类借阅统计</h2>
        <el-table v-loading="loading" :data="overview.categoryStats" border stripe>
          <el-table-column prop="categoryName" label="分类" />
          <el-table-column prop="borrowCount" label="借阅次数" width="100" />
        </el-table>
      </section>

      <section>
        <h2>用户借阅次数</h2>
        <el-table v-loading="loading" :data="overview.userStats" border stripe>
          <el-table-column prop="username" label="用户" />
          <el-table-column prop="borrowCount" label="借阅次数" width="100" />
          <el-table-column prop="overdueCount" label="逾期未还" width="100" />
        </el-table>
      </section>

      <section>
        <h2>近 7 天借阅趋势</h2>
        <el-table v-loading="loading" :data="overview.recentTrends" border stripe>
          <el-table-column prop="borrowDate" label="日期" />
          <el-table-column prop="borrowCount" label="借阅次数" width="100" />
        </el-table>
      </section>
    </div>
  </section>
</template>

<script setup>
import { onMounted, reactive, ref } from 'vue';
import { ElMessage } from 'element-plus';
import { getStatisticsOverview } from '../../api/statistics';

const loading = ref(false);
const overview = reactive({
  summary: {},
  hotBooks: [],
  categoryStats: [],
  userStats: [],
  recentTrends: []
});

async function load() {
  loading.value = true;
  try {
    const result = await getStatisticsOverview();
    if (!result.success) {
      ElMessage.error(result.message || '统计数据加载失败');
      return;
    }
    Object.assign(overview, result.data || {});
  } catch (error) {
    ElMessage.error('统计数据加载失败');
  } finally {
    loading.value = false;
  }
}

onMounted(load);
</script>

<style scoped>
.stat-row {
  margin-bottom: 20px;
}

.stat-box {
  min-height: 86px;
  padding: 16px;
  background: #ffffff;
  border: 1px solid #e5e7eb;
}

.stat-box span {
  display: block;
  color: #5f6b7a;
  margin-bottom: 8px;
}

.stat-box strong {
  font-size: 26px;
}

.stat-grid {
  display: grid;
  grid-template-columns: repeat(2, minmax(0, 1fr));
  gap: 20px;
}

.stat-grid h2 {
  margin: 0 0 12px;
  font-size: 18px;
}

@media (max-width: 900px) {
  .stat-grid {
    grid-template-columns: 1fr;
  }
}
</style>
