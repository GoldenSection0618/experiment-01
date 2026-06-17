import request from './request';

export function askBookIntro(bookId) {
  return request.post('/ai/book-intro', { bookId }, { timeout: 35000 });
}
