import request from '@/router/axios'

export const getPage = (page, params) => {
    return request({
        url: '/api/wms/receive/pageNotReceiveDetail',
        method: 'post',
        params: page,
        data: params
    })
}

export const exportExcel = (params) => {
    return request({
        url: '/api/wms/receive/exportNotReceiveDetail',
        method: 'post',
        data: params,
        responseType: 'blob'
    })
}
