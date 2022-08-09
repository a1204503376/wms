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

export const cancelTask = (data) => {
    return request({
        url: '/api/wms/taskDetail/cancelTask',
        method: 'post',
        data: {
            taskIdList: data
        }
    })
}

export const continueTask = (data) => {
    return request({
        url: '/api/wms/taskDetail/continueTask',
        method: 'post',
        data: {
            taskIdList: data
        }
    })
}
