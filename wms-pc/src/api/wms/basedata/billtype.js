import request from '@/router/axios';

export const getPage = (current, size, params) => {
  return request({
    url: '/api/wms/basedata/billtype/page',
    method: 'get',
    params: {
      ...params,
      current,
      size,
    }
  })
}

export const remove = (ids) => {
  return request({
    url: '/api/wms/basedata/billtype/remove',
    method: 'post',
    params: {
      ids,
    }
  })
}

export const add = (row) => {
  return request({
    url: '/api/wms/basedata/billtype/submit',
    method: 'post',
    data: row
  })
}

export const update = (row) => {
  return request({
    url: '/api/wms/basedata/billtype/update',
    method: 'post',
    data: row
  })
}

export const getDetail = (id) => {
  return request({
    url: '/api/wms/basedata/billtype/detail',
    method: 'get',
    params: {
      billTypeId: id
    }
  })
}


