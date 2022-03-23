import request from '@/router/axios'

//新增分类
export function add(row) {
    return request({
        url: '/api/wms/basedata/skutype/submit',
        method: 'post',
        data: row,
    })
}

export const getDetail = (skuTypeId) => {
    return request({
        url: '/api/wms/basedata/skutype/detail',
        method: 'get',
        params: {
            skuTypeId
        }
    })
}
export const getListTree = (params) => {
    return request({
        url: '/api/wms/basedata/skutype/tree',
        method: 'get',
        params: {
            ...params,
        }
    })
}
export const getList = (params) => {
    return request({
        url: '/api/wms/basedata/skutype/list',
        method: 'get',
        params: {
            ...params,
        }
    })
}
export function getPage(current, size, params) {
    return request({
        url: '/api/wms/basedata/skutype/page',
        method: 'get',
        params: {
            ...params,
            current,
            size,
        }
    })
}

export const remove = (ids) => {
    return request({
        url: '/api/wms/basedata/skutype/remove',
        method: 'post',
        params: {
            ids,
        }
    })
}
export const update = (row) => {
    return request({
        url: '/api/wms/basedata/skutype/submit',
        method: 'post',
        data: row
    })
}

export const getTree = (params) => {
    return request({
        url: '/api/wms/basedata/skutype/tree',
        method: 'get',
        params: {
            ...params
        }
    })
}

export const exportFile = (data) => {
    return request({
        url: '/api/wms/basedata/skutype/export',
        method: 'get',
        params: {
            ...data
        },
        responseType: 'blob'
    })
}
