import request from '@/router/axios';

export const getPage = (current, size, params) => {
  return request({
    url: '/api/wms/strategy/mission/page',
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
    url: '/api/wms/strategy/mission/detail',
    method: 'get',
    params: {
      id
    }
  })
}

export const remove = (ids) => {
  return request({
    url: '/api/wms/strategy/mission/remove',
    method: 'post',
    params: {
      ids,
    }
  })
}

export const submit = (row) => {
  return request({
    url: '/api/wms/strategy/mission/submit',
    method: 'post',
    data: row
  })
}

