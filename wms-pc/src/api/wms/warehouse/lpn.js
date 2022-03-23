import request from '@/router/axios'

export const getPage = (current, size, params) => {
  return request({
    url: '/api/wms/warehouse/lpn/page',
    method: 'get',
    params: {
      ...params,
      current,
      size,
    }
  })
}

export const getDetail = (lpnId) => {
  return request({
    url: '/api/wms/warehouse/lpn/detail',
    method: 'get',
    params: {
      lpnId
    }
  })
}

export const remove = (ids) => {
  return request({
    url: '/api/wms/warehouse/lpn/remove',
    method: 'post',
    params: {
      ids,
    }
  })
}

export const add = (row) => {
  return request({
    url: '/api/wms/warehouse/lpn/submit',
    method: 'post',
    data: row
  })
}

export const update = (row) => {
  return request({
    url: '/api/wms/warehouse/lpn/submit',
    method: 'post',
    data: row
  })
}
export const exportFile = (data) => {
    return request({
        url: '/api/wms/warehouse/lpn/export',
        method: 'get',
        params: {
            ...data
        },
        responseType: 'blob'
    })
}

