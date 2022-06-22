import request from '@/router/axios'

export const page = (page, params) => {
    return request({
        url: '/api/wms/log/logApiPage',
        method: 'post',
        params: page,
        data: params
    })
}
export const exportFile = (data) => {
    return request({
        url: '/api/wms/log/logApiExport',
        method: 'post',
        data: data,
        responseType: 'blob'
    })
}
