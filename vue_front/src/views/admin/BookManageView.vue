<template>
  <section class="page">
    <div class="page-header">
      <h1>图书管理</h1>
      <el-button type="primary" @click="openDialog()">新增图书</el-button>
    </div>
    <el-form :inline="true" :model="query">
      <el-form-item label="书名"><el-input v-model="query.title" clearable /></el-form-item>
      <el-form-item label="作者"><el-input v-model="query.author" clearable /></el-form-item>
      <el-form-item label="分类"><el-select v-model="query.categoryId" clearable><el-option v-for="c in categories" :key="c.id" :label="c.name" :value="c.id" /></el-select></el-form-item>
      <el-form-item label="状态"><el-select v-model="query.status" clearable><el-option label="上架" value="ON_SHELF" /><el-option label="下架" value="OFF_SHELF" /></el-select></el-form-item>
      <el-form-item><el-button type="primary" @click="load">查询</el-button></el-form-item>
    </el-form>
    <el-table :data="rows" border stripe>
      <el-table-column prop="title" label="书名" min-width="150" />
      <el-table-column prop="categoryName" label="分类" width="100" />
      <el-table-column prop="author" label="作者" />
      <el-table-column prop="isbn" label="ISBN" min-width="140" />
      <el-table-column prop="totalStock" label="总库存" width="90" />
      <el-table-column prop="availableStock" label="可借" width="80" />
      <el-table-column prop="status" label="状态" width="90" />
      <el-table-column label="操作" width="180">
        <template #default="{ row }">
          <el-button size="small" @click="openDialog(row)">编辑</el-button>
          <el-button size="small" :type="row.status === 'ON_SHELF' ? 'warning' : 'success'" @click="toggle(row)">
            {{ row.status === 'ON_SHELF' ? '下架' : '上架' }}
          </el-button>
        </template>
      </el-table-column>
    </el-table>
    <el-pagination layout="total, prev, pager, next" :total="total" :page-size="query.pageSize" v-model:current-page="query.pageNum" @current-change="load" />

    <el-dialog v-model="dialogVisible" :title="form.id ? '编辑图书' : '新增图书'" width="520px">
      <el-form ref="formRef" :model="form" :rules="rules" label-position="top">
        <el-form-item label="分类" prop="categoryId"><el-select v-model="form.categoryId"><el-option v-for="c in categories" :key="c.id" :label="c.name" :value="c.id" /></el-select></el-form-item>
        <el-form-item label="书名" prop="title"><el-input v-model="form.title" /></el-form-item>
        <el-form-item label="作者" prop="author"><el-input v-model="form.author" /></el-form-item>
        <el-form-item label="出版社"><el-input v-model="form.publisher" /></el-form-item>
        <el-form-item label="ISBN" prop="isbn"><el-input v-model="form.isbn" /></el-form-item>
        <el-form-item label="总库存" prop="totalStock"><el-input-number v-model="form.totalStock" :min="0" /></el-form-item>
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
import { listActiveCategories } from '../../api/categories';
import { createBook, listAdminBooks, updateBook, updateBookStatus } from '../../api/adminBooks';

const query = reactive({ title: '', author: '', categoryId: null, status: '', pageNum: 1, pageSize: 10 });
const rows = ref([]);
const categories = ref([]);
const total = ref(0);
const dialogVisible = ref(false);
const formRef = ref();
const form = reactive({ id: null, categoryId: null, title: '', author: '', publisher: '', isbn: '', totalStock: 0 });
const rules = {
  categoryId: [{ required: true, message: '请选择分类', trigger: 'change' }],
  title: [{ required: true, message: '请输入书名', trigger: 'blur' }],
  author: [{ required: true, message: '请输入作者', trigger: 'blur' }],
  isbn: [{ required: true, message: '请输入 ISBN', trigger: 'blur' }],
  totalStock: [{ required: true, message: '请输入库存', trigger: 'change' }]
};

async function loadCategories() {
  const result = await listActiveCategories();
  if (result.success) categories.value = result.data;
}
async function load() {
  const result = await listAdminBooks(query);
  if (result.success) {
    rows.value = result.data.list;
    total.value = result.data.total;
  }
}
function openDialog(row) {
  Object.assign(form, row ? { ...row } : { id: null, categoryId: null, title: '', author: '', publisher: '', isbn: '', totalStock: 0 });
  dialogVisible.value = true;
}
async function save() {
  await formRef.value.validate();
  const result = form.id ? await updateBook(form.id, form) : await createBook(form);
  if (!result.success) return ElMessage.error(result.message);
  ElMessage.success('保存成功');
  dialogVisible.value = false;
  load();
}
async function toggle(row) {
  const result = await updateBookStatus(row.id, row.status === 'ON_SHELF' ? 'OFF_SHELF' : 'ON_SHELF');
  if (!result.success) return ElMessage.error(result.message);
  load();
}
onMounted(() => {
  loadCategories();
  load();
});
</script>
