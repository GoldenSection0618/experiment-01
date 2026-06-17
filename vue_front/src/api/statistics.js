import request from './request';

export function getStatisticsOverview() {
  return request.get('/admin/statistics/overview');
}
