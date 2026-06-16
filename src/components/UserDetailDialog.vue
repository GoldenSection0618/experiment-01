<template>
  <el-dialog title="用户详情" :visible.sync="visible" width="500px" :close-on-click-modal="true">
    <el-descriptions v-if="user" :column="2" border>
      <el-descriptions-item label="编号">{{ user.id }}</el-descriptions-item>
      <el-descriptions-item label="用户名">{{ user.name }}</el-descriptions-item>
      <el-descriptions-item label="性别">{{ user.gender }}</el-descriptions-item>
      <el-descriptions-item label="年龄">{{ user.age }}</el-descriptions-item>
      <el-descriptions-item label="邮箱" :span="2">{{ user.email }}</el-descriptions-item>
      <el-descriptions-item label="地址" :span="2">{{ user.address }}</el-descriptions-item>
      <el-descriptions-item label="出生年月">{{ user.birthday }}</el-descriptions-item>
      <el-descriptions-item label="注册时间">{{ user.registerTime }}</el-descriptions-item>
    </el-descriptions>
    <span slot="footer">
      <el-button type="primary" @click="visible = false">关闭</el-button>
    </span>
  </el-dialog>
</template>

<script>
export default {
  name: 'UserDetailDialog',
  props: {
    user: { type: Object, default: null },
    value: { type: Boolean, default: false }
  },
  computed: {
    visible: {
      get() { return this.value; },
      set(v) { this.$emit('input', v); }
    }
  }
};
</script>
