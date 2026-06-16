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
          <el-form-item label="性别">
            <el-select v-model="search.gender" placeholder="请选择" clearable>
              <el-option label="男" value="男" />
              <el-option label="女" value="女" />
            </el-select>
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
            <el-table-column prop="name" label="用户名" min-width="100" align="center" />
            <el-table-column prop="gender" label="性别" width="70" align="center">
              <template slot-scope="{ row }">
                <el-tag :type="row.gender === '男' ? 'primary' : 'danger'" size="small" disable-transitions>{{ row.gender }}</el-tag>
              </template>
            </el-table-column>
            <el-table-column prop="age" label="年龄" width="70" align="center" sortable />
            <el-table-column prop="email" label="邮箱" min-width="180" align="center" show-overflow-tooltip />
            <el-table-column prop="address" label="地址" min-width="140" align="center" show-overflow-tooltip />
            <el-table-column label="操作" width="200" align="center" fixed="right">
              <template slot-scope="{ row }">
                <el-button type="text" size="small" icon="el-icon-view" @click="openDetail(row)">查看</el-button>
                <el-button type="text" size="small" icon="el-icon-edit" @click="handleEdit(row)">编辑</el-button>
                <el-button type="text" size="small" icon="el-icon-delete" style="color:#F56C6C" @click="handleDelete(row)">删除</el-button>
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
import { userList } from '../data/users';

export default {
  name: 'UserListPage',
  components: { AppLayout, UserDetailDialog },
  data() {
    return {
      allUsers: userList.slice(),
      search: { username: '', gender: '' },
      currentPage: 1,
      pageSize: 5,
      dialogVisible: false,
      currentUser: null
    };
  },
  computed: {
    filteredList() {
      let list = this.allUsers;
      if (this.search.username) {
        const kw = this.search.username.toLowerCase();
        list = list.filter(u => u.name.toLowerCase().includes(kw));
      }
      if (this.search.gender) {
        list = list.filter(u => u.gender === this.search.gender);
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
    handlePageChange(page) {
      this.currentPage = page;
    },
    handleSizeChange(size) {
      this.pageSize = size;
      this.currentPage = 1;
    },
    handleReset() {
      this.search = { username: '', gender: '' };
      this.currentPage = 1;
      this.$message.info('搜索条件已重置');
    },
    openDetail(row) {
      this.currentUser = row;
      this.dialogVisible = true;
    },
    handleEdit(row) {
      this.$message.info(`静态页面暂未实现编辑：${row.name}`);
    },
    handleDelete(row) {
      this.$confirm(`确定删除用户"${row.name}"？`, '提示', { confirmButtonText: '确定', cancelButtonText: '取消', type: 'warning' })
        .then(() => {
          this.allUsers = this.allUsers.filter(u => u.id !== row.id);
          this.$message.success(`已模拟删除用户：${row.name}`);
          if (!this.pagedList.length && this.currentPage > 1) this.currentPage--;
        })
        .catch(() => this.$message.info('已取消删除'));
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
