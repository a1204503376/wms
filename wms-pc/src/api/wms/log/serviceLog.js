import request from '@/router/axios'

export const getLogActionLists = (data,query) => {
    return request({
        url: '/api/wms/log/getLogActionLists',
        method: 'post',
        data: data,
        params: query,
    })
}

export const exportActionLists = (data) => {
    return request({
        url: '/api/wms/log/export',
        method: 'post',
        data: data,
        responseType: 'blob'
    })
}

