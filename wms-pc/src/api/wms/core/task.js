import request from '@/router/axios'

export const getList = (current, size, params) => {
    return request({
        url: '/api/wms/core/task/page',
        method: 'get',
        params: {
            ...params,
            current,
            size,
        }
    })
}

export const getDetail = (id) => {
    return request({
        url: '/api/wms/core/task/detail',
        method: 'get',
        params: {
            taskId: id
        }
    })
}

export const remove = (ids) => {
    return request({
        url: '/api/wms/core/task/remove',
        method: 'post',
        params: {
            ids,
        }
    })
}

export const add = (row) => {
    return request({
        url: '/api/wms/core/task/submit',
        method: 'post',
        data: row
    })
}

export const update = (row) => {
    return request({
        url: '/api/wms/core/task/submit',
        method: 'post',
        data: row
    })
}

export const changeUser = (ids, userId) => {
    return request({
        url: '/api/wms/core/task/changeUser',
        method: 'post',
        params: {
            ids,
            userId
        }
    })
}

export const close = (ids) => {
    return request({
        url: '/api/wms/core/task/close',
        method: 'post',
        params: {
            ids,
        }
    })
}

