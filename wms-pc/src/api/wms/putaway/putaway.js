import request from '@/router/axios';

export const getPage = (page, params) => {
    return request({
        url: '/api/wms/putaway/page',
        method: 'post',
        params: page,
        data: params
    })
}

export const exportFile = (data) => {
    return request({
        url: '/api/wms/putaway/export',
        method: 'post',
        data: data,
        responseType: 'blob'
    })
}
