import request from '@/router/axios'

export const getPage = (current, size, params) => {
  return request({
    url: '/api/wms/basedata/owner/page',
    method: 'get',
    params: {
      ...params,
      current,
      size,
    }
  })
}

export const getDetail = (woId) => {
  return request({
    url: '/api/wms/basedata/owner/detail',
    method: 'get',
    params: {
      woId
    }
  })
}

export const remove = (ids) => {
  return request({
    url: '/api/wms/basedata/owner/remove',
    method: 'post',
    params: {
      ids,
    }
  })
}

export const add = (row) => {
  return request({
    url: '/api/wms/basedata/owner/submit',
    method: 'post',
    data: row
  })
}

export const update = (row) => {
  return request({
    url: '/api/wms/basedata/owner/submit',
    method: 'post',
    data: row
  })
}

export const exportFile = (data) => {
  return request({
    url: '/api/wms/basedata/owner/export',
    method: 'get',
    params: {
      ...data
    },
    responseType: 'blob'
  })
}

