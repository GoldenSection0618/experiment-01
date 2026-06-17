import request from './request';

export function listAdminBooks(params) {
  return request.get('/admin/books', { params });
}

export function createBook(data) {
  return request.post('/admin/books', data);
}

export function updateBook(id, data) {
  return request.put(`/admin/books/${id}`, data);
}

export function updateBookStatus(id, status) {
  return request.put(`/admin/books/${id}/status`, null, { params: { status } });
}
