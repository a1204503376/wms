import request from '@/router/axios';

export const getList = (params) => {
  return request({
    url: '/api/report/reportfile/list',
    method: 'get',
    params: {
      ...params,
    }
  })
}

export const getDetail = (id) => {
  return request({
    url: '/api/report/reportfile/detail',
    method: 'get',
    params: {
      id
    }
  })
}

export const remove = (ids) => {
  return request({
    url: '/api/report/reportfile/remove',
    method: 'post',
    params: {
      ids,
    }
  })
}

export const submit = (row) => {
  return request({
    url: '/api/report/reportfile/submit',
    method: 'post',
    data: row
  })
}

