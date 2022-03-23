import request from '@/router/axios';

export const getPage = (current, size, params) => {
  return request({
    url: '/api/wms/crontab/task/page',
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
    url: '/api/wms/crontab/task/detail',
    method: 'get',
    params: {
      id
    }
  })
}

export const remove = (ids) => {
  return request({
    url: '/api/wms/crontab/task/remove',
    method: 'post',
    params: {
      ids,
    }
  })
}

export const submit = (row) => {
  return request({
    url: '/api/wms/crontab/task/submit',
    method: 'post',
    data: row
  })
}

