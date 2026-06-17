import request from './request';

export function listAdminBorrows(params) {
  return request.get('/admin/borrows', { params });
}
