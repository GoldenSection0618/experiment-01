import request from './request';

export function listCategories(params) {
  return request.get('/admin/categories', { params });
}

export function listActiveCategories() {
  return request.get('/admin/categories/active');
}

export function createCategory(data) {
  return request.post('/admin/categories', data);
}

export function updateCategory(id, data) {
  return request.put(`/admin/categories/${id}`, data);
}

export function updateCategoryStatus(id, status) {
  return request.put(`/admin/categories/${id}/status`, null, { params: { status } });
}
