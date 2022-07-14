import request from '@/router/axios'

export const page = (page, params) => {
    return request({
        url: '/api/wms/stock/page',
        method: 'post',
        params: page,
        data: params
    })
}
export const getStockStatusList = () => {
    return request({
        url: '/api/wms/state/getStockStatus',
        method: 'get'
    })
}
export const exportFile = (data) => {
    return request({
        url: '/api/wms/stock/export',
        method: 'post',
        data: data,
        responseType: 'blob'
    })
}

export const importFile = (data) => {
    return request({
        url: '/api/wms/stock/import-data',
        method: 'post',
        data: data
    })
}


