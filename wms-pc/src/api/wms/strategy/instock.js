import request from '@/router/axios'

export const getPage = (current, size, params) => {
    return request({
        url: '/api/wms/strategy/instock/page',
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
        url: '/api/wms/strategy/instock/list',
        method: 'get',
        params: {
            ...params
        }
    })
}

export const getDetail = (ssiId) => {
    return request({
        url: '/api/wms/strategy/instock/detail',
        method: 'get',
        params: {
            ssiId
        }
    })
}

export const remove = (ids) => {
    return request({
        url: '/api/wms/strategy/instock/remove',
        method: 'post',
        params: {
            ids,
        }
    })
}

export const add = (row) => {
    return request({
        url: '/api/wms/strategy/instock/submit',
        method: 'post',
        data: row
    })
}

export const update = (row) => {
    return request({
        url: '/api/wms/strategy/instock/submit',
        method: 'post',
        data: row
    })
}

export const instockDetailList = (params) => {
    return request({
        url: '/api/wms/strategy/instock/instockDetailList',
        method: 'get',
        params: {
            ...params
        }
    })
}

export const instockSkuDetailList = (params) => {
    return request({
        url: '/api/wms/strategy/instock/instockDetailList',
        method: 'get',
        params: {
            ...params
        }
    })
}

export const instockConfigLotList = (params) => {
    return request({
        url: '/api/wms/strategy/instock/instockDetailList',
        method: 'get',
        params: {
            ...params
        }
    })
}

