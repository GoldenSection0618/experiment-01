import request from './request';

export function listBooks() {
  return request.get('/books');
}
