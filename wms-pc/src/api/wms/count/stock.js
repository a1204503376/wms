import request from '@/router/axios';

export const getList = (current, size, params) => {
    return request({
        url: '/api/wms/stock/stock/page',
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
        url: '/api/wms/stock/stock/remove',
        method: 'post',
        params: {
            ids,
        }
    })
}
export const add = (row) => {
    return request({
        url: '/api/wms/stock/stock/save',
        method: 'post',
        data: row
    })
}
export const update = (row) => {
    return request({
        url: '/api/wms/stock/stock/update',
        method: 'post',
        data: row
    })
}