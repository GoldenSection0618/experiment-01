import request from './request';

export function borrowBook(bookId) {
  return request.post(`/borrows/${bookId}`);
}

export function listMyBorrows() {
  return request.get('/borrows/my');
}

export function renewBorrow(recordId) {
  return request.post(`/borrows/${recordId}/renew`);
}

export function returnBorrow(recordId) {
  return request.post(`/borrows/${recordId}/return`);
}
