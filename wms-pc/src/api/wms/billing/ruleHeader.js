import request from '@/router/axios';

export const getPage = (current, size, params) => {
  return request({
    url: '/api/wms/billing/rule-header/page',
    method: 'get',
    params: {
      ...params,
      current,
      size,
    }
  })
}

export const getDetail = (id) => {
  return request({
    url: '/api/wms/billing/rule-header/detail',
    method: 'get',
    params: {
      id
    }
  })
}

export const remove = (ids) => {
  return request({
    url: '/api/wms/billing/rule-header/remove',
    method: 'post',
    params: {
      ids,
    }
  })
}

export const submit = (row) => {
  return request({
    url: '/api/wms/billing/rule-header/submit',
    method: 'post',
    data: row
  })
}

