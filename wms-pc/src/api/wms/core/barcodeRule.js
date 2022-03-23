import request from '@/router/axios'

export const getList = (current, size, params) => {
    return request({
        url: '/api/wms/core/barcodeRule/page',
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
        url: '/api/wms/core/barcodeRule/remove',
        method: 'post',
        params: {
            ids,
        }
    })
}
export const add = (row) => {
    return request({
        url: '/api/wms/core/barcodeRule/submit',
        method: 'post',
        data: row
    })
}
export const update = (row) => {
    return request({
        url: '/api/wms/core/barcodeRule/submit',
        method: 'post',
        data: row
    })
}
export const getDetail = (sbrId) => {
    return request({
        url: '/api/wms/core/barcodeRule/detail',
        method: 'get',
        params: {
            sbrId
        }
    })
}
