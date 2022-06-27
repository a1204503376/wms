import request from '@/router/axios'

export const getPage = (page, params) => {
    return request({
        url: '/api/wms/receive/pageReceiveLog',
        method: 'post',
        params: page,
        data: params
    })
}

export const exportExcel = (params) => {
    return request({
        url: '/api/wms/receive/exportReceiveLog',
        method: 'post',
        data: params
    })
}
