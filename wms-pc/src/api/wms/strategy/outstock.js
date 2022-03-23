import request from '@/router/axios'

export const getPage = (current, size, params) => {
    return request({
        url: '/api/wms/strategy/outstock/page',
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
        url: '/api/wms/strategy/outstock/list',
        method: 'get',
        params: {
            ...params
        }
    })
}

export const getDetail = (ssoId) => {
    return request({
        url: '/api/wms/strategy/outstock/detail',
        method: 'get',
        params: {
            ssoId
        }
    })
}

export const remove = (ids) => {
    return request({
        url: '/api/wms/strategy/outstock/remove',
        method: 'post',
        params: {
            ids,
        }
    })
}

export const add = (row) => {
    return request({
        url: '/api/wms/strategy/outstock/submit',
        method: 'post',
        data: row
    })
}

