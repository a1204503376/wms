import request from '@/router/axios'

export const page = (data, params) => {
    return request({
        url: '/api/wms/skuBox/page',
        method: 'post',
        data: data,
        params: params
    })
}

export const exportExcel = (data) => {
    return request({
        url: '/api/wms/skuBox/export',
        method: 'post',
        data: data,
        responseType: 'blob'
    })
}
