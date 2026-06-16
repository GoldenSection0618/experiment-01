<template>
  <app-layout>
    <div class="user-list-page">
      <div class="page-header">
        <h2 class="page-title"><i class="el-icon-user-solid"></i>用户管理</h2>
        <el-breadcrumb separator="/">
          <el-breadcrumb-item :to="{ path: '/home' }">主页</el-breadcrumb-item>
          <el-breadcrumb-item>用户管理</el-breadcrumb-item>
        </el-breadcrumb>
      </div>

      <el-card class="search-card" shadow="never">
        <el-form :inline="true" :model="search" size="small">
          <el-form-item label="用户名">
            <el-input v-model="search.username" placeholder="请输入用户名" clearable />
          </el-form-item>
          <el-form-item label="城市">
            <el-input v-model="search.city" placeholder="请输入城市" clearable />
          </el-form-item>
          <el-form-item>
            <el-button type="primary" icon="el-icon-search" @click="handleSearch">搜索</el-button>
            <el-button icon="el-icon-refresh" @click="handleReset">重置</el-button>
          </el-form-item>
        </el-form>
      </el-card>

      <el-card class="table-card" shadow="never">
        <div class="table-container">
          <el-table :data="pagedList" stripe border :default-sort="{ prop: 'id', order: 'ascending' }">
            <el-table-column prop="id" label="编号" width="70" align="center" sortable />
            <el-table-column prop="date" label="日期" min-width="110" align="center" sortable />
            <el-table-column prop="name" label="姓名" min-width="100" align="center" />
            <el-table-column prop="province" label="省份" min-width="100" align="center" />
            <el-table-column prop="city" label="城市" min-width="100" align="center" />
            <el-table-column prop="address" label="地址" min-width="140" align="center" show-overflow-tooltip />
            <el-table-column prop="zip" label="邮编" min-width="100" align="center" />
            <el-table-column label="操作" width="180" align="center" fixed="right">
              <template slot-scope="{ row }">
                <el-button type="text" size="small" icon="el-icon-view" @click="openDetail(row)">查看</el-button>
              </template>
            </el-table-column>
          </el-table>
        </div>

        <div class="pagination-wrapper">
          <el-pagination
            background
            @size-change="handleSizeChange"
            @current-change="handlePageChange"
            :current-page="currentPage" :page-sizes="[5, 10, 15, 20]" :page-size="pageSize"
            layout="total, sizes, prev, pager, next, jumper" :total="filteredList.length" />
        </div>
      </el-card>

      <UserDetailDialog v-model="dialogVisible" :user="currentUser" />
    </div>
  </app-layout>
</template>

<script>
import AppLayout from '../components/AppLayout.vue';
import UserDetailDialog from '../components/UserDetailDialog.vue';
import { getUsers } from '../api/users';

export default {
  name: 'UserListPage',
  components: { AppLayout, UserDetailDialog },
  data() {
    return {
      allUsers: [],
      search: { username: '', city: '' },
      currentPage: 1,
      pageSize: 5,
      dialogVisible: false,
      currentUser: null
    };
  },
  created() {
    this.loadUsers();
  },
  computed: {
    filteredList() {
      let list = this.allUsers;
      if (this.search.username) {
        const kw = this.search.username.toLowerCase();
        list = list.filter(u => u.name.toLowerCase().includes(kw));
      }
      if (this.search.city) {
        const city = this.search.city.toLowerCase();
        list = list.filter(u => (u.city || '').toLowerCase().includes(city));
      }
      return list;
    },
    pagedList() {
      const start = (this.currentPage - 1) * this.pageSize;
      return this.filteredList.slice(start, start + this.pageSize);
    }
  },
  methods: {
    handleSearch() {
      this.currentPage = 1;
    },
    async loadUsers() {
      try {
        const { data: result } = await getUsers();
        if (!result.success) {
          this.$message.error(result.message);
          return;
        }
        this.allUsers = result.data || [];
      } catch (error) {
        this.$message.error('用户列表加载失败');
      }
    },
    handlePageChange(page) {
      this.currentPage = page;
    },
    handleSizeChange(size) {
      this.pageSize = size;
      this.currentPage = 1;
    },
    handleReset() {
      this.search = { username: '', city: '' };
      this.currentPage = 1;
      this.$message.info('搜索条件已重置');
    },
    openDetail(row) {
      this.currentUser = row;
      this.dialogVisible = true;
    }
  }
};
</script>

<style scoped>
.user-list-page  { max-width: 1400px; }
.search-card     { margin-bottom: 16px; border-radius: 10px; }
.table-card      { border-radius: 10px; }
.table-container { overflow-x: auto; }
.pagination-wrapper {
  display: flex;
  justify-content: flex-end;
  margin-top: 20px;
  padding-top: 16px;
  border-top: 1px solid #ebeef5;
}
</style>
