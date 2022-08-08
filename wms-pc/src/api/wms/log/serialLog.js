import request from '@/router/axios'

export const getPage = (page, params) => {
    return request({
        url: '/api/wms/serialLog/page',
        method: 'post',
        params: page,
        data: params
    })
}

export const exportExcel = (data) => {
    return request({
        url: '/api/wms/serialLog/export',
        method: 'post',
        data: data,
        responseType: 'blob'
    })
}
