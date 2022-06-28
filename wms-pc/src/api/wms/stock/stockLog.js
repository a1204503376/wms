import request from '@/router/axios'

export const page = (page, params) => {
    return request({
        url: '/api/wms/stockLog/page',
        method: 'post',
        params: page,
        data: params
    })
}

export const exportExcel = (params) => {
    return request({
        url: '/api/wms/stockLog/export',
        method: 'post',
        data: params,
        responseType: 'blob'
    })
}

export const getTypeList = () => {
    return request({
        url: '/api/wms/state/getStockLogType',
        method: 'post'
    })
}

