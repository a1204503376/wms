import request from '@/router/axios'

export const getPage = (current, size, params) => {
    return request({
        url: '/api/wms/core/relenishment/page',
        method: 'get',
        params: {
            ...params,
            current,
            size,
        }
    })
}
export const canEdit = (id) => {
    return request({
        url: '/api/wms/core/relenishment/canEdit',
        method: 'get',
        params: {
            relBillId: id
        }
    })
}
export const add = (row) => {
    return request({
        url: '/api/wms/core/relenishment/submit',
        method: 'post',
        data: row
    })
}
export const relTask = (ids) => {
    return request({
        url: '/api/wms/core/relenishment/relTask',
        method: 'post',
        params: {
            ids:ids
        }
    })
}
export const checkTask = (ids) => {
    return request({
        url: '/api/wms/core/relenishment/checkTask',
        method: 'post',
        params: {
            ids:ids
        }
    })
}
export const getDetail = (row) => {
    return request({
        url: '/api/wms/core/relenishment/detail',
        method: 'get',
        params: {
            relBillId: row
        }
    })
}
export const getRelBillNo = (row) => {
    return request({
        url: '/api/wms/core/relenishment/getRelBillNo',
        method: 'get',
    })
}
export const getDefaultUser = (row) => {
    return request({
        url: '/api/wms/core/relenishment/defaultUser',
        method: 'get',
    })
}
export const remove = (ids) => {
    return request({
        url: '/api/wms/core/relenishment/remove',
        method: 'post',
        params: {
            ids,
        }
    })
}
