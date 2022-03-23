import request from '@/router/axios'

export const getPage = (current, size, params) => {
  return request({
    url: '/api/wms/warehouse/workarea/page',
    method: 'get',
    params: {
      ...params,
      current,
      size,
    }
  })
}

export const getDetail = (wwaId) => {
  return request({
    url: '/api/wms/warehouse/workarea/detail',
    method: 'get',
    params: {
      wwaId
    }
  })
}

export const remove = (ids) => {
  return request({
    url: '/api/wms/warehouse/workarea/remove',
    method: 'post',
    params: {
      ids,
    }
  })
}

export const add = (row) => {
  return request({
    url: '/api/wms/warehouse/workarea/submit',
    method: 'post',
    data: row
  })
}

export const update = (row) => {
  return request({
    url: '/api/wms/warehouse/workarea/submit',
    method: 'post',
    data: row
  })
}

