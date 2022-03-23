import request from '@/router/axios'

export const getPage = (current, size, params) => {
  return request({
    url: '/api/wms/basedata/sku-um/page',
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
    url: '/api/wms/basedata/sku-um/remove',
    method: 'post',
    params: {
      ids,
    }
  })
}
export const add = (row) => {
  return request({
    url: '/api/wms/basedata/sku-um/submit',
    method: 'post',
    data: row
  })
}
export const getList = (row) => {
  return request({
    url: '/api/wms/basedata/sku-um/list',
    method: 'get',
    data: row
  })
}

export const getDetail = (id) => {
  return request({
    url: '/api/wms/basedata/sku-um/detail',
    method: 'get',
    params: {
      wsuId: id
    }
  })
}

export const exportFile = (data) => {
  return request({
    url: '/api/wms/basedata/sku-um/export',
    method: 'get',
    params: {
      ...data
    },
    responseType: 'blob'
  })
}
