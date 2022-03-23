
import request from '@/router/axios'

export const getPage = (current, size, params) => {
  return request({
    url: '/api/wms/warehouse/warehouse/page',
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
    url: '/api/wms/warehouse/warehouse/detail',
    method: 'get',
    params: {
      id
    }
  })
}

export const remove = (ids) => {
  return request({
    url: '/api/wms/warehouse/warehouse/remove',
    method: 'post',
    params: {
      ids,
    }
  })
}

export const add = (row) => {
  return request({
    url: '/api/wms/warehouse/warehouse/submit',
    method: 'post',
    data: row
  })
}

export const update = (row) => {
  return request({
    url: '/api/wms/warehouse/warehouse/submit',
    method: 'post',
    data: row
  })
}

export const exportFile = (data) => {
  return request({
    url: '/api/wms/warehouse/warehouse/export',
    method: 'get',
    params: {
      ...data
    },
    responseType: 'blob'
  })
}
