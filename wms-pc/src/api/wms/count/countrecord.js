import request from '@/router/axios';

export const getList = (current, size, params) => {
    return request({
        url: '/api/wms/count/countrecord/page',
        method: 'get',
        params: {
            ...params,
            current,
            size,
        }
    })
}

export const exportFile = (data) => {
    return request({
        url: '/api/wms/count/countrecord/export',
        method: 'get',
        params: {
            ...data
        },
        responseType: 'blob'
    })
}