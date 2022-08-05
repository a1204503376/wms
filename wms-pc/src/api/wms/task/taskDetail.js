import request from '@/router/axios';

export const getPage = (page, params) => {
    return request({
        url: '/api/wms/taskDetail/page',
        method: 'post',
        params: page,
        data: params
    })
}

export const exportFile = (params) => {
    return request({
        url: '/api/wms/taskDetail/export',
        method: 'post',
        data: params,
        responseType: 'blob'
    })
}
export const getTaskTypeSelectResponse = (data) => {
    return request({
        url: '/api/wms/taskDetail/getTaskTypeSelectResponse',
        method: 'post',
        data: data
    })
}
export const getTaskStateSelectResponse = (data) => {
    return request({
        url: '/api/wms/taskDetail/getTaskStateSelectResponse',
        method: 'post',
        data: data
    })
}
