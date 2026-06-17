import request from './request';

export function listBooks(params) {
  return request.get('/books', { params });
}

export function listBookCategories() {
  return request.get('/books/categories');
}
