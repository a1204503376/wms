import request from '@/router/axios'

export const getPage = (current, size, params) => {
    return request({
        url: '/api/wms/warehouse/location/page',
        method: 'get',
        params: {
            ...params,
            current,
            size,
        }
    })
}
export const getList = (params) => {
    return request({
        url: '/api/wms/warehouse/location/list',
        method: 'get',
        params: {
            ...params
        }
    })
}
export const getDetail = (locId) => {
    return request({
        url: '/api/wms/warehouse/location/detail',
        method: 'get',
        params: {
            locId
        }
    })
}

export const remove = (ids) => {
    return request({
        url: '/api/wms/warehouse/location/remove',
        method: 'post',
        params: {
            ids,
        }
    })
}

export const add = (row) => {
    return request({
        url: '/api/wms/warehouse/location/submit',
        method: 'post',
        data: row
    })
}

export const update = (row) => {
    return request({
        url: '/api/wms/warehouse/location/submit',
        method: 'post',
        data: row
    })
}

export const print = (ids) => {
    return request({
        url: '/api/wms/warehouse/location/print',
        method: 'post',
        params: {
            ids,
        }
    })
}
export const getListByZoneId = (zoneId) => {
    return request({
      url: '/api/wms/warehouse/location/list?zoneId=' + zoneId,
      method: 'get'
    })
  }
export const exportFile = (data) => {
    return request({
        url: '/api/wms/warehouse/location/export',
        method: 'get',
        params: {
            ...data
        },
        responseType: 'blob'
    })
}
