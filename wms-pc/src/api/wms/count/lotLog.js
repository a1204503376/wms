import request from '@/router/axios';

export const getPage = (current, size, params) => {
  return request({
    url: '/api/wms/stock/lotlog/page',
    method: 'get',
    params: {
      ...params,
      current,
      size,
    }
  })
}

export const advancedSearch = (current, size, params) => {
  return request({
    url: '/api/wms/stock/lotlog/advancedSearch',
    method: 'get',
    params: {
      ...params,
      current,
      size,
    }
  })
}