import request from '@/router/axios'

export const getPage = (current, size, params) => {
  return request({
    url: '/api/wms/basedata/affairscausecode/page',
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
    url: '/api/wms/basedata/affairscausecode/remove',
    method: 'post',
    params: {
      ids,
    }
  })
}
export const add = (row) => {
  return request({
    url: '/api/wms/basedata/affairscausecode/submit',
    method: 'post',
    data: row
  })
}
export const update = (row) => {
  return request({
    url: '/api/wms/basedata/affairscausecode/submit',
    method: 'post',
    data: row
  })
}

export const getDetail = (id) => {
  return request({
    url: '/api/wms/basedata/affairscausecode/detail',
    method: 'get',
    params: {
      saccId: id
    }
  })
}
