import request from './request';

export function listAdminUsers(params) {
  return request.get('/admin/users', { params });
}

export function updateUserStatus(id, status) {
  return request.put(`/admin/users/${id}/status`, null, { params: { status } });
}

export function getUserDebt(id) {
  return request.get(`/admin/users/${id}/debt`);
}

export function clearUserDebt(id) {
  return request.post(`/admin/users/${id}/debt/clear`);
}
