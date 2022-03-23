import request from '@/router/axios';

export const getPage = (current, size, params) => {
    return request({
        url: '/system/printtemplate/page',
        method: 'get',
        params: {
            ...params,
            current,
            size,
        }
    })
}

export const getList = (params) => {
    return request({
        url: '/api/wms/system/printtemplate/list',
        method: 'get',
        params: {
            ...params,
        }
    })
}

export const getDetail = (id) => {
    return request({
        url: '/system/printtemplate/detail',
        method: 'get',
        params: {
            sptId: id
        }
    })
}

export const remove = (ids) => {
    return request({
        url: '/system/printtemplate/remove',
        method: 'post',
        params: {
            ids,
        }
    })
}

export const add = (row) => {
    return request({
        url: '/system/printtemplate/submit',
        method: 'post',
        data: row
    })
}

export const update = (row) => {
    return request({
        url: '/system/printtemplate/submit',
        method: 'post',
        data: row
    })
}

