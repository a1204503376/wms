import request from '@/router/axios';

export const getPage = (current, size, params) => {
    return request({
        url: '/api/wms/taskDetail/page',
        method: 'post',
        params: size,
        data: current
    })
}

export const exportTaskDetail = (params) => {
    return request({
        url: '/api/wms/taskDetail/export',
        method: 'post',
        data: params,
        responseType: 'blob'
    })
}
