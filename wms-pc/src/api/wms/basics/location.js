import request from '@/router/axios'

export const getPage = (page, params) => {
    return request({
        url: '/api/wms/warehouse/location/page',
        method: 'post',
        params: page,
        data: params
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

export const getLocationSelectResponseTop10List = (data) => {
    return request({
        url: '/api/wms/warehouse/location/getLocationSelectResponseTop10List',
        method: 'post',
        data: data
    })
}

export const exportFile = (data) => {
    return request({
        url: '/api/wms/warehouse/location/export',
        method: 'post',
        data: data,
        responseType: 'blob'
    })
}

export const importFile = (data) => {
    return request({
        url: '/api/wms/warehouse/location/import-data',
        method: 'post',
        data: data
    })
}


