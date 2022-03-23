import request from '@/router/axios';

export const getList = (current, size, params) => {
  return request({
    url: '/api/wms/stock/stockOccupy/list',
    method: 'get',
    params: {
      ...params,
      current,
      size,
    }
  })
}