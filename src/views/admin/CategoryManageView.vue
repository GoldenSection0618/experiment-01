<template>
  <section class="page">
    <div class="page-header">
      <h1>分类管理</h1>
      <el-button type="primary" @click="openDialog()">新增分类</el-button>
    </div>
    <el-form :inline="true" :model="query">
      <el-form-item label="名称"><el-input v-model="query.name" clearable /></el-form-item>
      <el-form-item label="状态">
        <el-select v-model="query.status" clearable>
          <el-option label="启用" value="ACTIVE" />
          <el-option label="禁用" value="DISABLED" />
        </el-select>
      </el-form-item>
      <el-form-item><el-button type="primary" @click="load">查询</el-button></el-form-item>
    </el-form>
    <el-table :data="rows" border stripe>
      <el-table-column prop="name" label="名称" />
      <el-table-column prop="description" label="说明" />
      <el-table-column prop="status" label="状态" width="100" />
      <el-table-column label="操作" width="180">
        <template #default="{ row }">
          <el-button size="small" @click="openDialog(row)">编辑</el-button>
          <el-button size="small" :type="row.status === 'ACTIVE' ? 'warning' : 'success'" @click="toggle(row)">
            {{ row.status === 'ACTIVE' ? '禁用' : '启用' }}
          </el-button>
        </template>
      </el-table-column>
    </el-table>
    <el-pagination layout="total, prev, pager, next" :total="total" :page-size="query.pageSize" v-model:current-page="query.pageNum" @current-change="load" />

    <el-dialog v-model="dialogVisible" :title="form.id ? '编辑分类' : '新增分类'" width="420px">
      <el-form ref="formRef" :model="form" :rules="rules" label-position="top">
        <el-form-item label="名称" prop="name"><el-input v-model="form.name" /></el-form-item>
        <el-form-item label="说明"><el-input v-model="form.description" /></el-form-item>
        <el-form-item label="状态">
          <el-select v-model="form.status">
            <el-option label="启用" value="ACTIVE" />
            <el-option label="禁用" value="DISABLED" />
          </el-select>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="save">保存</el-button>
      </template>
    </el-dialog>
  </section>
</template>

<script setup>
import { onMounted, reactive, ref } from 'vue';
import { ElMessage } from 'element-plus';
import { createCategory, listCategories, updateCategory, updateCategoryStatus } from '../../api/categories';

const query = reactive({ name: '', status: '', pageNum: 1, pageSize: 10 });
const rows = ref([]);
const total = ref(0);
const dialogVisible = ref(false);
const formRef = ref();
const form = reactive({ id: null, name: '', description: '', status: 'ACTIVE' });
const rules = { name: [{ required: true, message: '请输入分类名称', trigger: 'blur' }, { min: 1, max: 50, message: '长度不能超过 50', trigger: 'blur' }] };

async function load() {
  const result = await listCategories(query);
  if (result.success) {
    rows.value = result.data.list;
    total.value = result.data.total;
  }
}
function openDialog(row) {
  Object.assign(form, row ? { ...row } : { id: null, name: '', description: '', status: 'ACTIVE' });
  dialogVisible.value = true;
}
async function save() {
  await formRef.value.validate();
  const result = form.id ? await updateCategory(form.id, form) : await createCategory(form);
  if (!result.success) return ElMessage.error(result.message);
  ElMessage.success('保存成功');
  dialogVisible.value = false;
  load();
}
async function toggle(row) {
  const result = await updateCategoryStatus(row.id, row.status === 'ACTIVE' ? 'DISABLED' : 'ACTIVE');
  if (!result.success) return ElMessage.error(result.message);
  load();
}
onMounted(load);
</script>
