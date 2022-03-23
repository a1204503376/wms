import request from '@/router/axios'

export const getPage = (current, size, params) => {
    return request({
        url: '/api/wms/strategy/relenishment/page',
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
        url: '/api/wms/strategy/relenishment/list',
        method: 'get',
        params: {
            ...params
        }
    })
}

export const getDetail = (ssiId) => {
    return request({
        url: '/api/wms/strategy/relenishment/detail',
        method: 'get',
        params: {
            ssiId
        }
    })
}

export const remove = (ids) => {
    return request({
        url: '/api/wms/strategy/relenishment/remove',
        method: 'post',
        params: {
            ids,
        }
    })
}

export const add = (row) => {
    return request({
        url: '/api/wms/strategy/relenishment/submit',
        method: 'post',
        data: row
    })
}

export const update = (row) => {
    return request({
        url: '/api/wms/strategy/relenishment/submit',
        method: 'post',
        data: row
    })
}

export const relenishmentDetailList = (params) => {
    return request({
        url: '/api/wms/strategy/relenishment/instockDetailList',
        method: 'get',
        params: {
            ...params
        }
    })
}





