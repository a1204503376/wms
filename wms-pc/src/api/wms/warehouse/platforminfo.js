import request from '@/router/axios'

export const getList = (params) => {
    return request({
        url: '/api/wms/warehouse/platforminfo/list',
        method: 'get',
        params: {
            ...params
        }
    })
}

export const getPage = (current, size, params) => {
  return request({
    url: '/api/wms/warehouse/platforminfo/page',
    method: 'get',
    params: {
      ...params,
      current,
      size,
    }
  })
}

export const getDetail = (spiId) => {
    return request({
        url: '/api/wms/warehouse/platforminfo/detail',
        method: 'get',
        params: {
            spiId
        }
    })
}

export const remove = (ids) => {
    return request({
        url: '/api/wms/warehouse/platforminfo/remove',
        method: 'post',
        params: {
            ids,
        }
    })
}

export const add = (row) => {
    return request({
        url: '/api/wms/warehouse/platforminfo/submit',
        method: 'post',
        data: row
    })
}

export const update = (row) => {
    return request({
        url: '/api/wms/warehouse/platforminfo/submit',
        method: 'post',
        data: row
    })
}
export const exportFile = (data) => {
    return request({
        url: '/api/wms/warehouse/platforminfo/export',
        method: 'get',
        params: {
            ...data
        },
        responseType: 'blob'
    })
}
